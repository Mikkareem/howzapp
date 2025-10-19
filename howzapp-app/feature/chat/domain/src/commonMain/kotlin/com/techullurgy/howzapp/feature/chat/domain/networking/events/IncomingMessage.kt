package com.techullurgy.howzapp.feature.chat.domain.networking.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface IncomingMessage {
    @Serializable
    @SerialName("typing")
    data class TypingMessage(val chatId: String, val userId: String) : IncomingMessage

    @Serializable
    @SerialName("recording_audio")
    data class RecordingAudioMessage(val chatId: String, val userId: String) : IncomingMessage

    @Serializable
    @SerialName("online_indicator")
    data class OnlineIndicatorMessage(val userId: String) : IncomingMessage

    @Serializable
    @SerialName("offline_indicator")
    data class OfflineIndicatorMessage(val userId: String) : IncomingMessage

    @Serializable
    @SerialName("message_sync")
    data object NotifyMessageSyncMessage : IncomingMessage
}