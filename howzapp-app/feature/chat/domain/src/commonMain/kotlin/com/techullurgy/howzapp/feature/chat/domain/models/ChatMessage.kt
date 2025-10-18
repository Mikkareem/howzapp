package com.techullurgy.howzapp.feature.chat.domain.models

import kotlin.time.Instant

data class ChatMessage(
    val messageId: String,
    val chatId: String,
    val content: Message,
    val owner: MessageOwner,
    val timestamp: Instant,
) {
    companion object
}