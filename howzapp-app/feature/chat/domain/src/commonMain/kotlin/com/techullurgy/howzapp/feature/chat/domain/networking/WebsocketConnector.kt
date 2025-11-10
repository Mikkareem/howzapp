package com.techullurgy.howzapp.feature.chat.domain.networking

import com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState
import com.techullurgy.howzapp.feature.chat.domain.networking.events.IncomingMessage
import com.techullurgy.howzapp.feature.chat.domain.networking.events.OutgoingMessage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface WebsocketConnector {
    val connectionState: StateFlow<ConnectionState>
    val messages: SharedFlow<IncomingMessage>

    suspend fun sendOutgoingMessage(message: OutgoingMessage)
}