package com.techullurgy.howzapp.core.network

import com.techullurgy.howzapp.common.utils.models.AppResult
import com.techullurgy.howzapp.common.utils.models.DataError
import com.techullurgy.howzapp.common.utils.models.onFailure
import com.techullurgy.howzapp.common.utils.models.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.content.OutgoingContent
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import io.ktor.utils.io.writeFully
import io.ktor.utils.io.writer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import org.koin.core.annotation.Single

@Single
class NetworkClient internal constructor(
    @PublishedApi internal val client: HttpClient
) {
    suspend fun websocketSession(
        url: String,
        token: String
    ): WebsocketSession {
        return client.webSocketSession(
            route = url
        ) {
            header("Authorization", "Bearer $token")
        }.run {
            KtorWebsocketSession(this)
        }
    }

    suspend inline fun <reified Request : Any, reified Response : Any> post(
        route: String,
        body: Request,
        headers: Map<String, String> = mapOf(),
        queryParams: Map<String, String> = mapOf()
    ): AppResult<Response, DataError.Remote> {
        return client.post<Request, Response>(
            route = route,
            body = body,
            queryParams = queryParams
        ) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }
    }

    suspend inline fun <reified Response : Any> get(
        route: String,
        headers: Map<String, String> = mapOf(),
        queryParams: Map<String, String> = mapOf()
    ): AppResult<Response, DataError.Remote> {
        return client.get<Response>(
            route = route,
            queryParams = queryParams
        ) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }
    }

    suspend inline fun <reified Response : Any> delete(
        route: String,
        headers: Map<String, String> = mapOf(),
        queryParams: Map<String, String> = mapOf()
    ): AppResult<Response, DataError.Remote> {
        return client.delete<Response>(
            route = route,
            queryParams = queryParams
        ) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }
    }

    suspend inline fun <reified Request : Any, reified Response : Any> put(
        route: String,
        body: Request,
        headers: Map<String, String> = mapOf(),
        queryParams: Map<String, String> = mapOf()
    ): AppResult<Response, DataError.Remote> {
        return client.put<Request, Response>(
            route = route,
            body = body,
            queryParams = queryParams
        ) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }
    }

    fun upload(
        url: String,
        source: ByteArray,
        headers: Map<String, String>
    ): Flow<UploadStatus> = callbackFlow {
        send(UploadStatus.Started)

        client.post<Unit, String>(
            route = url,
            body = Unit
        ) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }.onSuccess { uploadUrl ->
            val content = object : OutgoingContent.ReadChannelContent() {
                override fun readFrom(): ByteReadChannel {
                    return writer {
                        val buffer = ByteArray(4096)
                        var sentBytes = 0L

                        val channelSource = ByteReadChannel(source)

                        while (isActive) {
                            val read = channelSource.readAvailable(buffer)
                            if (read == -1) break
                            channel.writeFully(buffer)
                            sentBytes += read
                            val percentage = (sentBytes.toDouble() / source.size.toDouble()) * 100.0
                            send(UploadStatus.Progress(percentage))
                        }
                    }.channel
                }
            }

            client.put<OutgoingContent.ReadChannelContent, String>(
                route = "",
                body = content
            ) {
                timeout {
                    requestTimeoutMillis = 60_000
                }
                url(uploadUrl)
            }.onSuccess {
                send(UploadStatus.Success(it))
            }.onFailure {
                send(UploadStatus.Failed)
            }
        }.onFailure {
            send(UploadStatus.Failed)
        }

        awaitClose {
            // Nothing to do now
        }
    }

    fun clearAuthToken() {
        client.clearAuthToken()
    }

    sealed interface UploadStatus {
        data object Started : UploadStatus
        data class Progress(val percentage: Double) : UploadStatus
        data class Success(val url: String) : UploadStatus
        data object Failed : UploadStatus
        data object Cancelled : UploadStatus
    }
}