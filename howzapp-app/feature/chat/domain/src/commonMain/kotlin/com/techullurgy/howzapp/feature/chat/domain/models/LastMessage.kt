package com.techullurgy.howzapp.feature.chat.domain.models

import kotlin.time.Instant

sealed interface LastMessage {
    val timestamp: Instant

    data class MessageWithStatus(
        val message: Message,
        val status: MessageStatus,
        override val timestamp: Instant
    ): LastMessage

    data class MessageWithNoStatus(
        val message: Message,
        override val timestamp: Instant
    ): LastMessage
}