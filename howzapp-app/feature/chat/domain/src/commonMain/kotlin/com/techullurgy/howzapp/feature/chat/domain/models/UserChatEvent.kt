package com.techullurgy.howzapp.feature.chat.domain.models


enum class UserChatEventType {
    TYPING, RECORDING_AUDIO
}

data class UserChatEvent(
    val chatId: String,
    val userId: String,
    val eventType: UserChatEventType
)