package com.techullurgy.howzapp.feature.chat.data.dto.models

import com.techullurgy.howzapp.feature.chat.data.dto.utils.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
internal data class ChatMessageDto(
    val messageId: String,
    val chatId: String,
    val message: MessageDto,
    val sender: UserDto,
    val status: MessageStatusDto?,
    val receipt: ReceiptDto?,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant,
)