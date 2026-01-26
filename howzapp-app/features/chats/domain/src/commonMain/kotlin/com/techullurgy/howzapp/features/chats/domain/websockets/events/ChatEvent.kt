package com.techullurgy.howzapp.features.chats.domain.websockets.events

sealed interface ChatEvent {
    sealed interface IncomingEvent : ChatEvent {
        data class TypingEvent(val chatId: String, val userId: String) : IncomingEvent
        data class RecordingAudioEvent(val chatId: String, val userId: String) : IncomingEvent
        data class OnlineIndicatorEvent(val userId: String) : IncomingEvent
        data class OfflineIndicatorEvent(val userId: String) : IncomingEvent
        data object NotifyMessageSyncEvent : IncomingEvent
    }

    sealed interface OutgoingEvent : ChatEvent {
        data class TypingEvent(val chatId: String) : OutgoingEvent
        data class RecordingAudioEvent(val chatId: String) : OutgoingEvent
        data class ParticipantSubscriptionEvent(val participants: List<String>) : OutgoingEvent
        data class ChatsSubscriptionEvent(val chats: List<String>) : OutgoingEvent
    }
}