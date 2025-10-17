package com.techullurgy.howzapp.feature.chat.data.dto.responses

import com.techullurgy.howzapp.feature.chat.data.dto.models.ChatMessageDto
import kotlinx.serialization.Serializable

@Serializable
internal data class LoadChatMessagesResponse(
    val hasPreviousAvailable: Boolean,
    val messages: List<ChatMessageDto>
)