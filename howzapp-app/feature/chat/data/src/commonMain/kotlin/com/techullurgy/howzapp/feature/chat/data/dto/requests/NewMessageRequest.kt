package com.techullurgy.howzapp.feature.chat.data.dto.requests

import com.techullurgy.howzapp.feature.chat.data.dto.models.MessageDto
import kotlinx.serialization.Serializable

@Serializable
internal data class NewMessageRequest(
    val chatId: String,
    val localMessageId: String,
    val message: MessageDto
)
