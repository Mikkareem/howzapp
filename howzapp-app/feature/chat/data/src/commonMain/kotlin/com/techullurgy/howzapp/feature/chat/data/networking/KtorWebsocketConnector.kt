package com.techullurgy.howzapp.feature.chat.data.networking

import com.techullurgy.howzapp.core.data.networking.UrlConstants
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.feature.chat.data.lifecycle.AppLifecycleObserver
import com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState
import com.techullurgy.howzapp.feature.chat.domain.networking.WebsocketConnector
import com.techullurgy.howzapp.feature.chat.domain.networking.events.IncomingMessage
import com.techullurgy.howzapp.feature.chat.domain.networking.events.OutgoingMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Qualifier
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

@Qualifier
annotation class WebsocketJson

@Single(binds = [WebsocketConnector::class])
internal class KtorWebsocketConnector(
    private val client: HttpClient,
    applicationScope: CoroutineScope,
    sessionStorage: SessionStorage,
    @param:WebsocketJson private val json: Json,
    private val connectionErrorHandler: ConnectionErrorHandler,
    private val connectionRetryHandler: ConnectionRetryHandler,
    appLifecycleObserver: AppLifecycleObserver,
    connectivityObserver: ConnectivityObserver
): WebsocketConnector {
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)

    override val connectionState = _connectionState.asStateFlow()

    private var currentSession: WebSocketSession? = null

    private val isConnected = connectivityObserver
        .isConnected
        .debounce(1.seconds)
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    private val isInForeground = appLifecycleObserver
        .isInForeground
        .onEach {
            if(it) {
                connectionRetryHandler.resetDelay()
            }
        }
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    override val messages = combine(
        sessionStorage.observeAuthInfo(),
        isConnected,
        isInForeground
    ) { authInfo, isConnected, isInForeground ->
        when {
            authInfo == null -> {
                _connectionState.value = ConnectionState.DISCONNECTED
                currentSession?.close()
                currentSession = null
                connectionRetryHandler.resetDelay()
                null
            }

            !isInForeground -> {
                _connectionState.value = ConnectionState.DISCONNECTED
                currentSession?.close()
                currentSession = null
                null
            }

            !isConnected -> {
                _connectionState.value = ConnectionState.ERROR_NETWORK
                currentSession?.close()
                currentSession = null
                null
            }

            else -> {
                if(_connectionState.value !in listOf(
                        ConnectionState.CONNECTING,
                        ConnectionState.CONNECTED
                    )
                ) {
                    _connectionState.value = ConnectionState.CONNECTING
                }

                authInfo
            }
        }
    }.flatMapLatest { authInfo ->
        if(authInfo == null) {
            emptyFlow()
        } else {
            createWebSocketFlow(authInfo.accessToken)
                // Catch block to transform exceptions for platform compatibility
                .catch { e ->
                    currentSession?.close()
                    currentSession = null

                    val transformedException = connectionErrorHandler.transformException(e)
                    throw transformedException
                }
                .retryWhen { t, attempt ->
                    val shouldRetry = connectionRetryHandler.shouldRetry(t, attempt)

                    if(shouldRetry) {
                        _connectionState.value = ConnectionState.CONNECTING
                        connectionRetryHandler.applyRetryDelay(attempt)
                    }

                    shouldRetry
                }
                // Catch block for non-retriable errors
                .catch { e ->
                    _connectionState.value = connectionErrorHandler.getConnectionStateForError(e)
                }
        }
    }
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
        )

    override suspend fun sendOutgoingMessage(message: OutgoingMessage) {
        currentSession?.let {
            if(it.isActive) {
                it.send(
                    Frame.Text(
                        json.encodeToString(message)
                    )
                )
            }
        }
    }

    private fun createWebSocketFlow(accessToken: String) = callbackFlow {
        _connectionState.value = ConnectionState.CONNECTING

        currentSession = client.webSocketSession(
            urlString = "${UrlConstants.BASE_URL_WS}/ws"
        ) {
            header("Authorization", "Bearer $accessToken")
        }

        currentSession?.let { session ->
            _connectionState.value = ConnectionState.CONNECTED

            session
                .incoming
                .consumeAsFlow()
                .buffer(
                    capacity = 100
                )
                .collect { frame ->
                    when(frame) {
                        is Frame.Text -> {
                            val incomingMessage = json.decodeFromString<IncomingMessage>(frame.readText())
                            send(incomingMessage)
                        }

                        is Frame.Ping -> {
                            session.send(Frame.Pong(frame.data))
                        }

                        else -> Unit
                    }
                }
        } ?: throw Exception("Failed to establish Websocket Session")

        awaitClose {
            launch(NonCancellable) {
                _connectionState.value = ConnectionState.DISCONNECTED
                currentSession?.close()
                currentSession = null
            }
        }
    }
}