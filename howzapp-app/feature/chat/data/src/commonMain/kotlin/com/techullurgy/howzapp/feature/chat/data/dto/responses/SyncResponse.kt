package com.techullurgy.howzapp.feature.chat.data.dto.responses

import com.techullurgy.howzapp.feature.chat.data.dto.models.ChatDto
import kotlinx.serialization.Serializable

@Serializable
internal data class SyncResponse(
    val chats: List<ChatDto>,
    val lastSyncTimestamp: Long
)