package com.techullurgy.howzapp.core.data.impl

import com.techullurgy.howzapp.common.models.ConnectionState
import com.techullurgy.howzapp.core.data.api.AppConnectivityObserver
import com.techullurgy.howzapp.core.data.api.AppLifecycleObserver
import com.techullurgy.howzapp.core.data.api.AppWebsocketConnector
import com.techullurgy.howzapp.core.network.AppWebsocketClient
import com.techullurgy.howzapp.core.network.Frame
import com.techullurgy.howzapp.core.network.WebsocketSession
import com.techullurgy.howzapp.core.session.SessionNotifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

@Single
internal class DefaultAppWebsocketConnector(
    private val client: AppWebsocketClient,
    applicationScope: CoroutineScope,
    sessionNotifier: SessionNotifier,
    private val connectionErrorHandler: ConnectionErrorHandler,
    private val connectionRetryHandler: ConnectionRetryHandler,
    appLifecycleObserver: AppLifecycleObserver,
    connectivityObserver: AppConnectivityObserver,
) : AppWebsocketConnector {
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private var currentSession: WebsocketSession? = null

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
        sessionNotifier.observeSessionInfo(),
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
                if (_connectionState.value !in listOf(
                        ConnectionState.CONNECTING,
                        ConnectionState.CONNECTED
                    )
                ) {
                    _connectionState.value = ConnectionState.CONNECTING
                }

                authInfo
            }
        }
    }
        .distinctUntilChanged()
        .flatMapLatest { authInfo ->
            if (authInfo == null) {
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

                        if (shouldRetry) {
                            _connectionState.value = ConnectionState.CONNECTING
                            connectionRetryHandler.applyRetryDelay(attempt)
                        }

                        shouldRetry
                    }
                    // Catch block for non-retriable errors
                    .catch { e ->
                        _connectionState.value =
                            connectionErrorHandler.getConnectionStateForError(e)
                    }
            }
        }
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
        )

    override suspend fun sendOutgoingMessage(message: String) {
        currentSession?.let {
            if(it.isActive) {
                it.send(
                    Frame.Text(message)
                )
            }
        }
    }

    private fun createWebSocketFlow(accessToken: String) = callbackFlow {
        _connectionState.value = ConnectionState.CONNECTING

        currentSession = client.websocketSession(
            url = "/ws",
            token = accessToken
        )

        currentSession?.let { session ->
            _connectionState.value = ConnectionState.CONNECTED

            session
                .incoming
                .buffer(
                    capacity = 100
                )
                .collect { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            send(frame.text)
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