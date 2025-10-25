package com.techullurgy.howzapp.core.dto.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface WebsocketIncomingMessage {
    @Serializable
    @SerialName("typing")
    data class TypingMessage(val chatId: String, val userId: String) : WebsocketIncomingMessage

    @Serializable
    @SerialName("recording_audio")
    data class RecordingAudioMessage(val chatId: String, val userId: String) :
        WebsocketIncomingMessage

    @Serializable
    @SerialName("online_indicator")
    data class OnlineIndicatorMessage(val userId: String) : WebsocketIncomingMessage

    @Serializable
    @SerialName("offline_indicator")
    data class OfflineIndicatorMessage(val userId: String) : WebsocketIncomingMessage

    @Serializable
    @SerialName("message_sync")
    data object NotifyMessageSyncMessage : WebsocketIncomingMessage
}