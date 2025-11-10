package com.techullurgy.howzapp.core.dto.responses

import com.techullurgy.howzapp.core.dto.models.ChatMessageDto
import kotlinx.serialization.Serializable

@Serializable
data class LoadChatMessagesResponse(
    val hasPreviousAvailable: Boolean,
    val messages: List<ChatMessageDto>
)