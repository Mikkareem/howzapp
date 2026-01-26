package com.techullurgy.howzapp.features.chats.models

import kotlin.time.Instant

data class ChatParticipant(
    val userId: String,
    val username: String,
    val profilePictureUrl: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: Instant? = null,
)