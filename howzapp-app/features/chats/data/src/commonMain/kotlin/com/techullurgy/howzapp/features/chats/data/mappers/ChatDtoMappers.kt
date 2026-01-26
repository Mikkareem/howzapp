package com.techullurgy.howzapp.features.chats.data.mappers

import com.techullurgy.howzapp.common.dto.UserDto
import com.techullurgy.howzapp.features.chats.data.dto.AudioMessageDto
import com.techullurgy.howzapp.features.chats.data.dto.ChatDto
import com.techullurgy.howzapp.features.chats.data.dto.ChatMessageDto
import com.techullurgy.howzapp.features.chats.data.dto.ChatTypeDto
import com.techullurgy.howzapp.features.chats.data.dto.ContactMessageDto
import com.techullurgy.howzapp.features.chats.data.dto.DirectChatDto
import com.techullurgy.howzapp.features.chats.data.dto.DocumentMessageDto
import com.techullurgy.howzapp.features.chats.data.dto.GroupChatDto
import com.techullurgy.howzapp.features.chats.data.dto.ImageMessageDto
import com.techullurgy.howzapp.features.chats.data.dto.LocationMessageDto
import com.techullurgy.howzapp.features.chats.data.dto.MessageDto
import com.techullurgy.howzapp.features.chats.data.dto.MessageStatusDto
import com.techullurgy.howzapp.features.chats.data.dto.ReceiptDto
import com.techullurgy.howzapp.features.chats.data.dto.TextMessageDto
import com.techullurgy.howzapp.features.chats.data.dto.VideoMessageDto
import com.techullurgy.howzapp.features.chats.models.Chat
import com.techullurgy.howzapp.features.chats.models.ChatInfo
import com.techullurgy.howzapp.features.chats.models.ChatMessage
import com.techullurgy.howzapp.features.chats.models.ChatParticipant
import com.techullurgy.howzapp.features.chats.models.ChatType
import com.techullurgy.howzapp.features.chats.models.MessageOwner
import com.techullurgy.howzapp.features.chats.models.MessageStatus
import com.techullurgy.howzapp.features.chats.models.OriginalMessage
import com.techullurgy.howzapp.features.chats.models.OriginalMessage.*

internal fun ChatDto.toDomain(): Chat {
    return Chat(
        chatInfo = ChatInfo(
            chatId = chatType.chatId,
            chatType = chatType.toDomain(),
        ),
        chatMessages = messages.map { it.toDomain() }
    )
}

internal fun ChatMessageDto.toDomain(): ChatMessage {

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
        is AudioMessageDto -> AudioMessage(audioUrl = audioUrl)
        is VideoMessageDto -> VideoMessage(videoUrl = videoUrl)
        is ContactMessageDto -> TODO()
        is DocumentMessageDto -> DocumentMessage(
            documentUrl = documentUrl,
            documentName = documentName
        )

        is ImageMessageDto -> ImageMessage(imageUrl = imageUrl)
        is TextMessageDto -> TextMessage(text = text)
        is LocationMessageDto -> LocationMessage(latitude = latitude, longitude = longitude)
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

internal fun OriginalMessage.toDto(): MessageDto {
    return when(this) {
        is OriginalMessage.AudioMessage -> AudioMessageDto(audioUrl)
        is OriginalMessage.DocumentMessage -> DocumentMessageDto(documentUrl, documentName)
        is OriginalMessage.ImageMessage -> ImageMessageDto(imageUrl)
        is OriginalMessage.TextMessage -> TextMessageDto(text)
        is OriginalMessage.VideoMessage -> VideoMessageDto(videoUrl)
        is OriginalMessage.LocationMessage -> LocationMessageDto(latitude, longitude)
    }
}