package com.techullurgy.howzapp.feature.chat.data.dto.models

import kotlinx.serialization.Serializable

@Serializable
internal data class ChatDto(
    val chatType: ChatTypeDto,
    val messages: List<ChatMessageDto>
)