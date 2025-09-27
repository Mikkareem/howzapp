package com.techullurgy.howzapp.notifications.websockets.models

data class IncomingMessage(
    val payload: String,
    val type: IncomingMessageType
)
