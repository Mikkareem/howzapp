package com.techullurgy.howzapp.core.data.impl

import com.techullurgy.howzapp.core.data.api.AppHttpConnector
import com.techullurgy.howzapp.core.data.api.AppUploadClient
import com.techullurgy.howzapp.core.network.NetworkClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ByteArrayUploadClient(
    private val connector: AppHttpConnector
) : AppUploadClient<ByteArray>() {
    override fun uploadMedia(
        source: ByteArray,
        sourceLength: Long,
        targetUrl: String,
        requestHeaders: Map<String, String>
    ): Flow<NetworkUploadStatus> {
        return connector.upload(
            url = targetUrl,
            source = source,
            headers = requestHeaders
        ).map {
            when (it) {
                NetworkClient.UploadStatus.Cancelled -> NetworkUploadStatus.Cancelled
                NetworkClient.UploadStatus.Failed -> NetworkUploadStatus.Failed
                is NetworkClient.UploadStatus.Progress -> NetworkUploadStatus.Progress(it.percentage)
                NetworkClient.UploadStatus.Started -> NetworkUploadStatus.Started
                is NetworkClient.UploadStatus.Success -> NetworkUploadStatus.Success(it.url)
            }
        }
    }
}