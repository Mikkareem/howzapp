package com.techullurgy.howzapp.feature.chat.domain.utils

import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import java.util.UUID
import kotlin.time.Clock

fun ChatMessage.Companion.newTextMessage(
    chatId: String,
    content: Message.TextMessage,
    me: ChatParticipant
): ChatMessage {
    return with(me) {
        ChatMessage.newMessage(chatId, content)
    }
}

fun ChatMessage.Companion.newAudioMessage(
    chatId: String,
    content: Message.AudioMessage,
    me: ChatParticipant
): ChatMessage {
    return with(me) {
        ChatMessage.newMessage(chatId, content)
    }
}

fun ChatMessage.Companion.newDocumentMessage(
    chatId: String,
    content: Message.DocumentMessage,
    me: ChatParticipant
): ChatMessage {
    return with(me) {
        ChatMessage.newMessage(chatId, content)
    }
}

fun ChatMessage.Companion.newImageMessage(
    chatId: String,
    content: Message.ImageMessage,
    me: ChatParticipant
): ChatMessage {
    return with(me) {
        ChatMessage.newMessage(chatId, content)
    }
}

fun ChatMessage.Companion.newVideoMessage(
    chatId: String,
    content: Message.VideoMessage,
    me: ChatParticipant
): ChatMessage {
    return with(me) {
        ChatMessage.newMessage(chatId, content)
    }
}

context(me: ChatParticipant)
internal fun ChatMessage.Companion.newMessage(
    chatId: String,
    content: Message
): ChatMessage {

    assert(content !is Message.PendingMessage) {
        "PendingMessage is not allowed here"
    }

    val pendingContent = if(content is Message.UploadableMessage) {
        Message.UploadablePendingMessage(
            uploadId = "",
            status = UploadStatus.Triggered,
            originalMessage = content,
            isReadyToSync = false
        )
    } else {
        Message.NonUploadablePendingMessage(
            originalMessage = content as Message.NonUploadableMessage,
            isReadyToSync = true
        )
    }

    return ChatMessage(
        messageId = "local_${UUID.randomUUID()}",
        chatId = chatId,
        content = pendingContent,
        owner = MessageOwner.Me(me, MessageStatus.PENDING),
        timestamp = Clock.System.now()
    )
}