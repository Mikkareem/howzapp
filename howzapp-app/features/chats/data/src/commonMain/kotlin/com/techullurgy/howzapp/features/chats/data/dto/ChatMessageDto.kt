package com.techullurgy.howzapp.features.chats.data.dto

import com.techullurgy.howzapp.common.dto.UserDto
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class ChatMessageDto(
    val messageId: String,
    val chatId: String,
    val message: MessageDto,
    val sender: UserDto,
    val status: MessageStatusDto?,
    val receipt: ReceiptDto?,
    val timestamp: Instant,
)