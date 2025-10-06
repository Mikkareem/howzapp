package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.models.SerializableMessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus

fun SerializableMessageStatus.toDomain(): MessageStatus {
    return when(this) {
        SerializableMessageStatus.PENDING -> MessageStatus.PENDING
        SerializableMessageStatus.CREATED -> MessageStatus.CREATED
        SerializableMessageStatus.SENT -> MessageStatus.SENT
        SerializableMessageStatus.DELIVERED -> MessageStatus.DELIVERED
        SerializableMessageStatus.READ -> MessageStatus.READ
    }
}

fun MessageStatus.toSerializable(): SerializableMessageStatus {
    return when(this) {
        MessageStatus.PENDING -> SerializableMessageStatus.PENDING
        MessageStatus.CREATED -> SerializableMessageStatus.CREATED
        MessageStatus.SENT -> SerializableMessageStatus.SENT
        MessageStatus.DELIVERED -> SerializableMessageStatus.DELIVERED
        MessageStatus.READ -> SerializableMessageStatus.READ
    }
}