package com.techullurgy.howzapp.features.chats.data.responses

import com.techullurgy.howzapp.features.chats.data.dto.ChatDto
import kotlinx.serialization.Serializable

@Serializable
data class SyncResponse(
    val chats: List<ChatDto>,
    val lastSyncTimestamp: Long
)