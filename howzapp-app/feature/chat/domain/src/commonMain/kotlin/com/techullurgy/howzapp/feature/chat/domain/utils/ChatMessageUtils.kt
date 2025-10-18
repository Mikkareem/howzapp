package com.techullurgy.howzapp.feature.chat.domain.utils

import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import java.util.UUID
import kotlin.time.Clock

fun ChatMessage.Companion.newTextMessage(
    chatId: String,
    content: OriginalMessage.TextMessage,
    me: ChatParticipant
): ChatMessage {
    return with(me) {
        ChatMessage.newMessage(chatId, content)
    }
}

fun ChatMessage.Companion.newAudioMessage(
    chatId: String,
    content: OriginalMessage.AudioMessage,
    me: ChatParticipant
): ChatMessage {
    return with(me) {
        ChatMessage.newMessage(chatId, content)
    }
}

fun ChatMessage.Companion.newDocumentMessage(
    chatId: String,
    content: OriginalMessage.DocumentMessage,
    me: ChatParticipant
): ChatMessage {
    return with(me) {
        ChatMessage.newMessage(chatId, content)
    }
}

fun ChatMessage.Companion.newImageMessage(
    chatId: String,
    content: OriginalMessage.ImageMessage,
    me: ChatParticipant
): ChatMessage {
    return with(me) {
        ChatMessage.newMessage(chatId, content)
    }
}

fun ChatMessage.Companion.newVideoMessage(
    chatId: String,
    content: OriginalMessage.VideoMessage,
    me: ChatParticipant
): ChatMessage {
    return with(me) {
        ChatMessage.newMessage(chatId, content)
    }
}

context(me: ChatParticipant)
internal fun ChatMessage.Companion.newMessage(
    chatId: String,
    content: OriginalMessage
): ChatMessage {
    val pendingContent = if(content !is OriginalMessage.TextMessage) {
        PendingMessage.UploadablePendingMessage(
            uploadId = "",
            status = UploadStatus.Triggered,
            originalMessage = content,
        )
    } else {
        PendingMessage.NonUploadablePendingMessage(
            originalMessage = content,
        )
    }

    return ChatMessage(
        messageId = UUID.randomUUID().toString(),
        chatId = chatId,
        content = pendingContent,
        owner = MessageOwner.Me(me, MessageStatus.SenderStatus.PENDING),
        timestamp = Clock.System.now()
    )
}