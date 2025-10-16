package com.techullurgy.howzapp.chats.api.dto

import com.techullurgy.howzapp.chats.models.Chat

data class SyncResponse(
    val chats: List<Chat>,
)
