package com.techullurgy.howzapp.feature.chat.data.dto.responses

import kotlinx.serialization.Serializable

@Serializable
internal data class NewMessageResponse(
    val localMessageId: String,
    val serverMessageId: String,
)
