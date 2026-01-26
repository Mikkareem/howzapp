package com.techullurgy.howzapp.features.chats.data.requests

import com.techullurgy.howzapp.features.chats.data.dto.ReceiptDto
import kotlinx.serialization.Serializable

@Serializable
data class MessageReceiptRequest(
    val messageId: String,
    val receipt: ReceiptDto
)