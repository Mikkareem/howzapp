package com.techullurgy.howzapp.core.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class LoadChatMessagesRequest(
    val chatId: String,
    val beforeMessage: String
)
