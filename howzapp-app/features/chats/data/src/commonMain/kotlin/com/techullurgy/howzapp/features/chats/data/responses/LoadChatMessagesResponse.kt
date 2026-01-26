package com.techullurgy.howzapp.features.chats.data.responses

import com.techullurgy.howzapp.features.chats.data.dto.ChatMessageDto
import kotlinx.serialization.Serializable

@Serializable
data class LoadChatMessagesResponse(
    val hasPreviousAvailable: Boolean,
    val messages: List<ChatMessageDto>
)