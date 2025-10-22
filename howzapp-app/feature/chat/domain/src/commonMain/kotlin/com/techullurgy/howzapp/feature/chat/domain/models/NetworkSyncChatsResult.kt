package com.techullurgy.howzapp.feature.chat.domain.models

data class NetworkSyncChatsResult(
    val chats: List<Chat>,
    val lastSyncTimestamp: Long
)
