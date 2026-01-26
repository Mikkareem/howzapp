package com.techullurgy.howzapp.features.chats.data.dto

import kotlinx.serialization.Serializable

@Serializable
enum class ReceiptDto {
    PENDING, DELIVERED, READ
}