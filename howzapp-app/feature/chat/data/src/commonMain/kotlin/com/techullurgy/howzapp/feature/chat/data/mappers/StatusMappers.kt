package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.entities.ReceiverStatus
import com.techullurgy.howzapp.feature.chat.database.entities.SenderStatus
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus

fun SenderStatus.toDomain(): MessageStatus.SenderStatus {
    return when(this) {
        SenderStatus.PENDING -> MessageStatus.SenderStatus.PENDING
        SenderStatus.SENT -> MessageStatus.SenderStatus.SENT
        SenderStatus.DELIVERED -> MessageStatus.SenderStatus.DELIVERED
        SenderStatus.READ -> MessageStatus.SenderStatus.READ
    }
}

fun ReceiverStatus.toDomain(): MessageStatus.ReceiverStatus {
    return when(this) {
        ReceiverStatus.READ -> MessageStatus.ReceiverStatus.READ
        ReceiverStatus.UNREAD -> MessageStatus.ReceiverStatus.UNREAD
    }
}

fun MessageStatus.ReceiverStatus.toSerializable(): ReceiverStatus {
    return when(this) {
        MessageStatus.ReceiverStatus.PENDING -> ReceiverStatus.UNREAD
        MessageStatus.ReceiverStatus.READ -> ReceiverStatus.READ
        MessageStatus.ReceiverStatus.UNREAD -> ReceiverStatus.UNREAD
    }
}

fun MessageStatus.SenderStatus.toSerializable(): SenderStatus {
    return when(this) {
        MessageStatus.SenderStatus.PENDING -> SenderStatus.PENDING
        MessageStatus.SenderStatus.READ -> SenderStatus.READ
        MessageStatus.SenderStatus.DELIVERED -> SenderStatus.DELIVERED
        MessageStatus.SenderStatus.SENT -> SenderStatus.SENT
    }
}