package com.techullurgy.howzapp.chats.infra.mappers

import com.techullurgy.howzapp.chats.infra.database.projections.ChatMessageProjection
import com.techullurgy.howzapp.chats.models.ChatMessage
import com.techullurgy.howzapp.users.infra.mappers.toDomain

fun ChatMessageProjection.toDomain(): ChatMessage {
    return ChatMessage(
        messageId = message.id,
        chatId = message.belongsToChat.id,
        message = message.toDomain(),
        sender = message.sender.toDomain(),
        status = status?.status,
        receipt = receipt?.receipt,
        timestamp = message.createdAt
    )
}