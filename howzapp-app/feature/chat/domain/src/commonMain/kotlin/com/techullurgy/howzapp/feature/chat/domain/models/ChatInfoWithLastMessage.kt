package com.techullurgy.howzapp.feature.chat.domain.models

data class ChatInfoWithLastMessage(
    val info: ChatInfo,
    val lastMessage: LastMessage,
    val unreadCount: Int
)