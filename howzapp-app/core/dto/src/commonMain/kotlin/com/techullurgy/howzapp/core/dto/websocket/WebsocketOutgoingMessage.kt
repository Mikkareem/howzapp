package com.techullurgy.howzapp.core.dto.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface WebsocketOutgoingMessage {
    @Serializable
    @SerialName("typing")
    data class TypingMessage(val chatId: String) : WebsocketOutgoingMessage

    @Serializable
    @SerialName("recording_audio")
    data class RecordingAudioMessage(val chatId: String) : WebsocketOutgoingMessage

    @Serializable
    @SerialName("participant_subscription")
    data class ParticipantSubscriptionMessage(val participants: List<String>) :
        WebsocketOutgoingMessage

    @Serializable
    @SerialName("chat_subscription")
    data class ChatsSubscriptionMessage(val chats: List<String>) : WebsocketOutgoingMessage
}