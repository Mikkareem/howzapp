package com.techullurgy.howzapp.chats.api.dto

import com.techullurgy.howzapp.chats.models.ChatMessage

data class LoadChatMessagesResponse(
    val hasPreviousAvailable: Boolean,
    val messages: List<ChatMessage>
)