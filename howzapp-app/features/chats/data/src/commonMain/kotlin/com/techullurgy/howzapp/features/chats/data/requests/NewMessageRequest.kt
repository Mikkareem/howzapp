package com.techullurgy.howzapp.features.chats.data.requests

import com.techullurgy.howzapp.features.chats.data.dto.MessageDto
import kotlinx.serialization.Serializable

@Serializable
data class NewMessageRequest(
    val chatId: String,
    val localMessageId: String,
    val message: MessageDto
)