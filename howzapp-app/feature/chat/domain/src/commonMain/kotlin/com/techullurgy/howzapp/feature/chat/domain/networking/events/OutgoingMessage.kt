package com.techullurgy.howzapp.feature.chat.domain.networking.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface OutgoingMessage {
    @Serializable
    @SerialName("typing")
    data class TypingMessage(val chatId: String) : OutgoingMessage

    @Serializable
    @SerialName("recording_audio")
    data class RecordingAudioMessage(val chatId: String) : OutgoingMessage

    @Serializable
    @SerialName("participant_subscription")
    data class ParticipantSubscriptionMessage(val participants: List<String>) : OutgoingMessage
}