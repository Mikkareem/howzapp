package com.techullurgy.howzapp.core.data.networking

import com.techullurgy.howzapp.core.domain.networking.NetworkUploadStatus
import com.techullurgy.howzapp.core.domain.networking.UploadClient
import com.techullurgy.howzapp.core.domain.util.onFailure
import com.techullurgy.howzapp.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.content.OutgoingContent
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import io.ktor.utils.io.writeFully
import io.ktor.utils.io.writer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive

internal class ByteArrayUploadClient(
    private val client: HttpClient
): UploadClient<ByteArray>() {
    override fun uploadMedia(
        source: ByteArray,
        sourceLength: Long,
        targetUrl: String,
        requestHeaders: Map<String, String>
    ): Flow<NetworkUploadStatus> = callbackFlow {
        send(NetworkUploadStatus.Started)

        client.post<Unit, String>(
            route = targetUrl,
            body = Unit
        ) {
            requestHeaders.forEach { key, value ->
                header(key, value)
            }
        }.onSuccess { uploadUrl ->
            val content = object : OutgoingContent.ReadChannelContent() {
                override fun readFrom(): ByteReadChannel {
                    return writer {
                        val buffer = ByteArray(4096)
                        var sentBytes = 0L

                        val channelSource = ByteReadChannel(source)

                        while(isActive) {
                            val read = channelSource.readAvailable(buffer)
                            if(read == -1) break
                            channel.writeFully(buffer)
                            sentBytes += read
                            val percentage = (sentBytes.toDouble() / sourceLength.toDouble()) * 100.0
                            send(NetworkUploadStatus.Progress(percentage))
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
                send(NetworkUploadStatus.Success(it))
            }.onFailure {
                send(NetworkUploadStatus.Failed)
            }
        }.onFailure {
            send(NetworkUploadStatus.Failed)
        }
    }
}