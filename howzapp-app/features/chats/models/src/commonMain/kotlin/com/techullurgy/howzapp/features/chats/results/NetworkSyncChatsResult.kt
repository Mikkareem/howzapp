package com.techullurgy.howzapp.features.chats.results

import com.techullurgy.howzapp.features.chats.models.Chat

data class NetworkSyncChatsResult(
    val chats: List<Chat>,
    val lastSyncTimestamp: Long
)
