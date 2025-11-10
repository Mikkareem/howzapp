package com.techullurgy.howzapp.core.dto.models

import kotlinx.serialization.Serializable

@Serializable
enum class ReceiptDto {
    PENDING, DELIVERED, READ
}