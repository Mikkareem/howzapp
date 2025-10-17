package com.techullurgy.howzapp.feature.chat.data.dto.requests

import com.techullurgy.howzapp.feature.chat.data.dto.models.ReceiptDto
import kotlinx.serialization.Serializable

@Serializable
internal data class MessageReceiptRequest(
    val messageId: String,
    val receipt: ReceiptDto
)
