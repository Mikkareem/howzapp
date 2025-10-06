package com.techullurgy.howzapp.feature.chat.domain.models

import kotlin.time.Instant

data class ChatMessage(
    val messageId: String,
    val chatId: String,
    val content: Message,
    val owner: MessageOwner,
    val timestamp: Instant,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChatMessage) return false

        if (messageId != other.messageId) return false

        return true
    }

    override fun hashCode(): Int {
        return messageId.hashCode()
    }

    companion object
}