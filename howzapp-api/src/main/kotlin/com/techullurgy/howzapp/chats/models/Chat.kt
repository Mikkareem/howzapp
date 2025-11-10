package com.techullurgy.howzapp.chats.models

data class Chat(
    val chatType: ChatType,
    val messages: List<ChatMessage>,
)