package com.techullurgy.howzapp.feature.chat.domain.networking.events

sealed interface IncomingMessage {
    data class TypingMessage(val chatId: String, val userId: String) : IncomingMessage
    data class RecordingAudioMessage(val chatId: String, val userId: String) : IncomingMessage
    data class OnlineIndicatorMessage(val userId: String) : IncomingMessage
    data class OfflineIndicatorMessage(val userId: String) : IncomingMessage
    data object NotifyMessageSyncMessage : IncomingMessage
}