package com.techullurgy.howzapp.features.chats.results

import com.techullurgy.howzapp.features.chats.models.ChatMessage

data class NetworkLoadMessagesResult(
    val messages: List<ChatMessage>,
    val hasPreviousAvailable: Boolean
)
