package com.techullurgy.howzapp.feature.chat.domain.networking.models

import com.techullurgy.howzapp.feature.chat.domain.models.Chat

data class NetworkSyncChatsResult(
    val chats: List<Chat>,
    val lastSyncTimestamp: Long
)
