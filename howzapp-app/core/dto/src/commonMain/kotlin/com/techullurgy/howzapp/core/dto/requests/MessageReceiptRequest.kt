package com.techullurgy.howzapp.core.dto.requests

import com.techullurgy.howzapp.core.dto.models.ReceiptDto
import kotlinx.serialization.Serializable

@Serializable
data class MessageReceiptRequest(
    val messageId: String,
    val receipt: ReceiptDto
)
