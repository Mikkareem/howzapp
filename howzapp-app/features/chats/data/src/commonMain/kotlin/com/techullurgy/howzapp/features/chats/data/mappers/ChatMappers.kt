package com.techullurgy.howzapp.features.chats.data.mappers

import com.techullurgy.howzapp.features.chats.database.entities.ChatRelation
import com.techullurgy.howzapp.features.chats.database.entities.DirectChatRelation
import com.techullurgy.howzapp.features.chats.database.entities.GroupChatRelation
import com.techullurgy.howzapp.features.chats.database.entities.MessageRelation
import com.techullurgy.howzapp.features.chats.database.entities.PendingMessageRelation
import com.techullurgy.howzapp.features.chats.models.Chat
import com.techullurgy.howzapp.features.chats.models.ChatInfo
import com.techullurgy.howzapp.features.chats.models.ChatMessage
import com.techullurgy.howzapp.features.chats.models.ChatType
import com.techullurgy.howzapp.features.chats.models.MessageOwner
import com.techullurgy.howzapp.features.chats.models.MessageStatus
import com.techullurgy.howzapp.features.chats.models.OriginalMessage
import com.techullurgy.howzapp.features.chats.models.PendingMessage
import kotlin.time.Instant

internal fun ChatRelation.toDomain(): Chat {
    return Chat(
        chatInfo = ChatInfo(
            chatId = chat.chatId,
            chatType = getChatType(),
        ),
        chatMessages = getAllMessages(),
    )
}

private fun ChatRelation.getChatType(): ChatType {
    return when {
        groupChat != null -> groupChat!!.toDomain()
        directChat != null -> directChat!!.toDomain()
        else -> TODO()
    }
}

private fun ChatRelation.getAllMessages(): List<ChatMessage> {
    return messages.map { it.toDomain() }.sortedByDescending { it.timestamp } +
            pendingMessages.map { it.toDomain() }.sortedByDescending { it.timestamp }
}

internal fun PendingMessageRelation.toDomain(): ChatMessage {
    return ChatMessage(
        messageId = pending.pendingId,
        chatId = pending.chatId,
        content = getMessage(),
        owner = MessageOwner.Me(sender.toDomain(), MessageStatus.SenderStatus.PENDING),
        timestamp = Instant.fromEpochMilliseconds(pending.timestamp)
    )
}

private fun PendingMessageRelation.getMessage(): PendingMessage {
    return if(uploadable != null) {
        PendingMessage.UploadablePendingMessage(
            status = uploadable!!.uploadStatus.toDomain(),
            originalMessage = pending.message.originalMessage.toDomain() as OriginalMessage.UploadableMessage
        )
    } else {
        PendingMessage.NonUploadablePendingMessage(
            originalMessage = pending.message.originalMessage.toDomain() as OriginalMessage.NonUploadableMessage
        )
    }
}

private fun MessageRelation.toDomain(): ChatMessage {
    return ChatMessage(
        messageId = message.messageId,
        chatId = message.chatId,
        content = message.message.toDomain(),
        owner = getOwner(),
        timestamp = Instant.fromEpochMilliseconds(message.timestamp)
    )
}

private fun MessageRelation.getOwner(): MessageOwner {
    return if(status.senderStatus != null) {
        MessageOwner.Me(
            owner = sender.toDomain(),
            status = status.senderStatus!!.status.toDomain()
        )
    } else {
        MessageOwner.Other(
            owner = sender.toDomain(),
            status = status.receiverStatus!!.status.toDomain()
        )
    }
}

private fun GroupChatRelation.toDomain(): ChatType {
    return ChatType.Group(
        title = chat.title,
        pictureUrl = chat.pictureUrl,
        participants = participants.map { it.toDomain() },
        originator = originator.toDomain()
    )
}

private fun DirectChatRelation.toDomain(): ChatType {
    return ChatType.Direct(
        me = me.toDomain(),
        other = other.toDomain()
    )
}