package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.data.dto.models.AudioMessageDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.ChatDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.ChatMessageDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.ChatTypeDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.ContactMessageDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.DirectChatDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.DocumentMessageDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.GroupChatDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.ImageMessageDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.MessageDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.MessageStatusDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.ReceiptDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.TextMessageDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.UserDto
import com.techullurgy.howzapp.feature.chat.data.dto.models.VideoMessageDto
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatInfo
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage

internal fun ChatDto.toDomain(): Chat {
    return Chat(
        chatInfo = ChatInfo(
            chatId = chatType.chatId,
            chatType = chatType.toDomain(),
        ),
        chatMessages = messages.map { it.toDomain() }
    )
}

private fun ChatMessageDto.toDomain(): ChatMessage {

    val senderStatus = status?.toDomain()
    val receiverStatus = when(receipt) {
        ReceiptDto.PENDING -> MessageStatus.ReceiverStatus.PENDING
        ReceiptDto.DELIVERED -> MessageStatus.ReceiverStatus.UNREAD
        ReceiptDto.READ -> MessageStatus.ReceiverStatus.READ
        null -> null
    }

    val owner = sender.toDomain()

    val messageOwner = when {
        senderStatus != null -> MessageOwner.Me(owner, senderStatus)
        receiverStatus != null -> MessageOwner.Other(owner, receiverStatus)
        else -> TODO()
    }

    return ChatMessage(
        messageId = messageId,
        chatId = chatId,
        content = message.toDomain(),
        owner = messageOwner,
        timestamp = timestamp,
    )
}

private fun MessageStatusDto.toDomain(): MessageStatus.SenderStatus {
    return when(this) {
        MessageStatusDto.SENT -> MessageStatus.SenderStatus.SENT
        MessageStatusDto.RECEIVED -> MessageStatus.SenderStatus.DELIVERED
        MessageStatusDto.READ -> MessageStatus.SenderStatus.READ
    }
}

private fun MessageDto.toDomain(): OriginalMessage {
    return when(this) {
        is AudioMessageDto -> OriginalMessage.AudioMessage(audioUrl = audioUrl)
        is VideoMessageDto -> OriginalMessage.VideoMessage(videoUrl = videoUrl)
        is ContactMessageDto -> TODO()
        is DocumentMessageDto -> OriginalMessage.DocumentMessage(documentUrl = documentUrl, documentName = documentName)
        is ImageMessageDto -> OriginalMessage.ImageMessage(imageUrl = imageUrl)
        is TextMessageDto -> OriginalMessage.TextMessage(text = text)
    }
}

private fun ChatTypeDto.toDomain(): ChatType {
    return when(this) {
        is DirectChatDto -> {
            ChatType.Direct(
                me = participant1.toDomain(),
                other = participant2.toDomain()
            )
        }
        is GroupChatDto -> {
            ChatType.Group(
                title = title,
                pictureUrl = profilePictureUrl,
                participants = participants.map { it.toDomain() },
                originator = originator.toDomain()
            )
        }
    }
}

internal fun UserDto.toDomain(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        username = name,
        profilePictureUrl = profilePictureUrl
    )
}