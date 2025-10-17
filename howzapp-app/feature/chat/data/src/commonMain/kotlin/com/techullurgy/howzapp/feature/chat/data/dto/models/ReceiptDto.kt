package com.techullurgy.howzapp.feature.chat.data.dto.models

import kotlinx.serialization.Serializable

@Serializable
internal enum class ReceiptDto {
    PENDING, DELIVERED, READ
}