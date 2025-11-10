package com.techullurgy.howzapp.feature.chat.domain.networking.models

import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage

data class NetworkLoadMessagesResult(
    val messages: List<ChatMessage>,
    val hasPreviousAvailable: Boolean
)
