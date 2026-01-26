package com.techullurgy.howzapp.features.chats.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val chatType: ChatTypeDto,
    val messages: List<ChatMessageDto>
)