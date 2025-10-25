package com.techullurgy.howzapp.core.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class NewMessageResponse(
    val localMessageId: String,
    val serverMessageId: String,
)
