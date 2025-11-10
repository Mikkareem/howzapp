package com.techullurgy.howzapp.core.dto.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val chatType: ChatTypeDto,
    val messages: List<ChatMessageDto>
)