package com.techullurgy.howzapp.features.chats.data.dto

import kotlinx.serialization.Serializable

@Serializable
enum class MessageStatusDto {
    SENT, RECEIVED, READ
}