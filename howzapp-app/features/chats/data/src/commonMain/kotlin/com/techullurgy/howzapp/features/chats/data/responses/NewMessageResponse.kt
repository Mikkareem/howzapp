package com.techullurgy.howzapp.features.chats.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class NewMessageResponse(
    val localMessageId: String,
    val serverMessageId: String,
)
