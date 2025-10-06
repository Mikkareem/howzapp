package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.views.ChatLastMessageView
import com.techullurgy.howzapp.feature.chat.domain.models.LastMessage
import kotlin.time.Instant

fun ChatLastMessageView.toDomain(): LastMessage {
    return messageStatus?.let {
        LastMessage.MessageWithStatus(
            status = it.toDomain(),
            message = lastMessage.toDomain(),
            timestamp = Instant.fromEpochMilliseconds(lastMessageTimestamp)
        )
    } ?: LastMessage.MessageWithNoStatus(
        message = lastMessage.toDomain(),
        timestamp = Instant.fromEpochMilliseconds(lastMessageTimestamp)
    )
}