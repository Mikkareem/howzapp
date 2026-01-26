package com.techullurgy.howzapp.features.chats.models

import kotlin.time.Instant

data class ChatPreview(
    val chatId: String,
    val title: String,
    val picture: String?,
    val unreadCount: Int,
    val chatType: ChatType,
    val lastMessage: ChatMessage,
    val lastMessageTimestamp: Instant,
)
