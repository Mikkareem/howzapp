package com.techullurgy.howzapp.core.dto.requests

import com.techullurgy.howzapp.core.dto.models.MessageDto
import kotlinx.serialization.Serializable

@Serializable
data class NewMessageRequest(
    val chatId: String,
    val localMessageId: String,
    val message: MessageDto
)
