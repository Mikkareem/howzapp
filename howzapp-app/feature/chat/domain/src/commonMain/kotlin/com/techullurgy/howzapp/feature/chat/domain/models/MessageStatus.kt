package com.techullurgy.howzapp.feature.chat.domain.models

sealed interface MessageStatus {
    enum class SenderStatus: MessageStatus {
        PENDING, SENT, DELIVERED, READ
    }

    enum class ReceiverStatus: MessageStatus {
        READ, UNREAD
    }
}