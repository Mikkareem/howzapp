package com.techullurgy.howzapp.features.chats.domain.websockets.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface WebsocketChatEvent {
    @Serializable
    sealed interface IncomingEvent : WebsocketChatEvent {
        @Serializable
        @SerialName("typing")
        data class TypingEvent(val chatId: String, val userId: String) : IncomingEvent

        @Serializable
        @SerialName("recording_audio")
        data class RecordingAudioEvent(val chatId: String, val userId: String) : IncomingEvent

        @Serializable
        @SerialName("online_indicator")
        data class OnlineIndicatorEvent(val userId: String) : IncomingEvent

        @Serializable
        @SerialName("offline_indicator")
        data class OfflineIndicatorEvent(val userId: String) : IncomingEvent

        @Serializable
        @SerialName("message_sync")
        data object NotifyMessageSyncEvent : IncomingEvent
    }

    @Serializable
    sealed interface OutgoingEvent : WebsocketChatEvent {
        @Serializable
        @SerialName("typing")
        data class TypingEvent(val chatId: String) : OutgoingEvent

        @Serializable
        @SerialName("recording_audio")
        data class RecordingAudioEvent(val chatId: String) : OutgoingEvent

        @Serializable
        @SerialName("participant_subscription")
        data class ParticipantSubscriptionEvent(val participants: List<String>) : OutgoingEvent

        @Serializable
        @SerialName("chat_subscription")
        data class ChatsSubscriptionEvent(val chats: List<String>) : OutgoingEvent
    }
}