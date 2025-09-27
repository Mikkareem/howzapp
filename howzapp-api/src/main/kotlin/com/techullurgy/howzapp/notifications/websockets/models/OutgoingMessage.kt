package com.techullurgy.howzapp.notifications.websockets.models

data class OutgoingMessage(
    val payload: String,
    val type: OutgoingMessageType
)