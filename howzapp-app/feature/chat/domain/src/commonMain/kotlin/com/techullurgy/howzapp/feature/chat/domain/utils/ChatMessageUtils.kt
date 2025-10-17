package com.techullurgy.howzapp.feature.chat.domain.utils

import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
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

    assert(content !is OriginalMessage.PendingMessage) {
        "PendingMessage is not allowed here"
    }

    val pendingContent = if(content is OriginalMessage.UploadableMessage) {
        OriginalMessage.UploadablePendingMessage(
            uploadId = "",
            status = UploadStatus.Triggered,
            originalMessage = content,
            isReadyToSync = false
        )
    } else {
        OriginalMessage.NonUploadablePendingMessage(
            originalMessage = content as OriginalMessage.NonUploadableMessage,
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