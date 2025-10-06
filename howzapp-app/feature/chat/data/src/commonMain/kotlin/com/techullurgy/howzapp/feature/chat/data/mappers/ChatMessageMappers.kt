package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatMessageStatusEntity
import com.techullurgy.howzapp.feature.chat.database.views.ChatMessageView
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import kotlin.time.Instant

fun ChatMessageView.toChatMessage() = ChatMessage(
    chatId = message.chatId,
    messageId = message.messageId,
    content = message.message.toDomain(),
    owner = if(status == null) {
        MessageOwner.Other(
            owner = sender.toDomain(),
            isReadByMe = false
        )
    } else {
        MessageOwner.Me(msgStatus = status!!.status.toDomain(), owner = sender.toDomain())
    },
    timestamp = Instant.fromEpochMilliseconds(message.timestamp),
)

fun ChatMessage.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = messageId,
        chatId = chatId,
        senderId = owner.owner.userId,
        message = content.toSerializable(),
        timestamp = timestamp.toEpochMilliseconds(),
        isRead = (owner as? MessageOwner.Other)?.isReadByMe ?: true
    )
}

fun ChatMessage.toStatusEntity(): ChatMessageStatusEntity? {
    return when(owner) {
        is MessageOwner.Me -> (owner as MessageOwner.Me).msgStatus.toSerializable()
        is MessageOwner.Other -> null
    }?.run {
        ChatMessageStatusEntity(messageId, this)
    }
}