package com.techullurgy.howzapp.core.data.api

import com.techullurgy.howzapp.common.models.ConnectionState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AppWebsocketConnector {
    val connectionState: StateFlow<ConnectionState>
    val messages: SharedFlow<String>
    suspend fun sendOutgoingMessage(message: String)
}