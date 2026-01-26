package com.techullurgy.howzapp.features.chats.models

data class Chat(
    val chatInfo: ChatInfo,
    val chatMessages: List<ChatMessage>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Chat) return false

        if (chatInfo.chatId != other.chatInfo.chatId) return false

        return true
    }

    override fun hashCode(): Int {
        return chatInfo.chatId.hashCode()
    }
}
