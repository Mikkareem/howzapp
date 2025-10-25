package com.techullurgy.howzapp.core.dto.responses

import com.techullurgy.howzapp.core.dto.models.ChatDto
import kotlinx.serialization.Serializable

@Serializable
data class SyncResponse(
    val chats: List<ChatDto>,
    val lastSyncTimestamp: Long
)