package com.techullurgy.howzapp.chats.services

import com.techullurgy.howzapp.chats.api.dto.MessageDto
import com.techullurgy.howzapp.chats.infra.database.entities.Receipt
import com.techullurgy.howzapp.chats.models.Chat
import com.techullurgy.howzapp.chats.models.ChatMessage
import com.techullurgy.howzapp.chats.services.usecases.*
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ChatService(
    private val loadNewMessagesForUserUsecase: LoadNewMessagesForUserUsecase,
    private val loadMessagesByIdsUsecase: LoadMessagesByIdsUsecase,
    private val loadMessageFromChatBeforeMessageUsecase: LoadMessageFromChatBeforeMessageUsecase,
    private val updateMessageReceiptUsecase: UpdateMessageReceiptUsecase,
    private val newMessageUsecase: NewMessageUsecase,
) {
    @Transactional
    fun loadNewMessagesForUser(
        userId: UserId,
        after: Instant
    ): List<Chat> = loadNewMessagesForUserUsecase(userId, after)

    fun loadMessagesFromChatBefore(
        userId: UserId,
        chatId: ChatId,
        lastMessage: MessageId
    ): List<ChatMessage> = loadMessageFromChatBeforeMessageUsecase(userId, chatId, lastMessage)

    fun loadMessagesByIdsForUser(
        userId: UserId,
        messages: List<MessageId>
    ): List<ChatMessage> = loadMessagesByIdsUsecase(userId, messages)

    @Transactional
    fun onNewMessage(
        from: UserId,
        chatId: ChatId,
        message: MessageDto,
    ) = newMessageUsecase(from, chatId, message)

    @Transactional
    fun updateMessageReceipt(
        participantId: UserId,
        messageId: MessageId,
        receipt: Receipt
    ) = updateMessageReceiptUsecase(participantId, messageId, receipt)
}