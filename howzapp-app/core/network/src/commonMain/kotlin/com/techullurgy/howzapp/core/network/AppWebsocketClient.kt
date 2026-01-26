package com.techullurgy.howzapp.core.network

import org.koin.core.annotation.Single

@Single
class AppWebsocketClient(
    private val client: NetworkClient
) {
    suspend fun websocketSession(
        url: String,
        token: String
    ): WebsocketSession {
        return client.websocketSession(url, token)
    }
}