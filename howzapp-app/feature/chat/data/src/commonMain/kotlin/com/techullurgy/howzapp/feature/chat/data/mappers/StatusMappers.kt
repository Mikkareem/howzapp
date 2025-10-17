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