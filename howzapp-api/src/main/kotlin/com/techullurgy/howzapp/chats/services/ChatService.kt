package com.techullurgy.howzapp.chats.services

import com.techullurgy.howzapp.chats.api.dto.MessageDto
import com.techullurgy.howzapp.chats.api.dto.NewMessageRequest
import com.techullurgy.howzapp.chats.events.ChatEvent
import com.techullurgy.howzapp.chats.infra.database.entities.*
import com.techullurgy.howzapp.chats.infra.database.entities.chats.GroupChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.chats.OneToOneChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.messages.*
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageReceiptsRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageStatusRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatRepository
import com.techullurgy.howzapp.chats.infra.mappers.toDomain
import com.techullurgy.howzapp.chats.models.ChatMessage
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import com.techullurgy.howzapp.users.infra.database.repositories.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.jvm.optionals.getOrNull
import kotlin.uuid.Uuid

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val messageRepository: ChatMessageRepository,
    private val messageStatusRepository: ChatMessageStatusRepository,
    private val messageReceiptsRepository: ChatMessageReceiptsRepository,
    private val appEventPublisher: ApplicationEventPublisher
) {
    @Transactional
    fun loadNewMessagesForUser(
        userId: UserId,
        instantAfter: Instant
    ): List<ChatMessage> {
        val user = userRepository.findById(userId).getOrNull() ?: return emptyList()
        val fetchedMessages = messageRepository.fetchNewMessagesForUser(user, instantAfter)
        return fetchedMessages.map { it.toDomain() }
    }

    @Transactional
    fun getNotifiableParticipantsForChat(chatId: ChatId, fromId: UserId): List<UserId> {
        return getParticipantsForChat(chatId, fromId)
            .map { it.id }
    }

    @Transactional
    fun getSenderOfTheMessage(messageId: MessageId): UserId {
        return messageRepository.findById(messageId).get().sender.id
    }

    @Transactional
    fun onNewMessage(from: UserId, request: NewMessageRequest) {
        val sender = userRepository.findById(from).get()

        val desiredChat = getDesiredChat(request, sender)

        val messageEntity = getDesiredMessage(request, sender, desiredChat)

        ChatMessageStatusEntity(
            message = messageEntity,
            sender = sender,
            status = MessageStatus.SENT
        ).also {
            messageStatusRepository.save(it)
        }

        getParticipantsForChat(desiredChat.id, sender.id).forEach { participant ->
            ChatMessageReceiptsEntity(
                id = Uuid.id.toString(),
                message = messageEntity,
                user = participant,
                receipt = Receipt.PENDING
            ).also {
                messageReceiptsRepository.save(it)
            }
        }

        appEventPublisher.publishEvent(
            ChatEvent.OnNewMessage(messageEntity.id, desiredChat.id, sender.id)
        )
    }

    @Transactional
    fun updateMessageReceipt(
        participantId: UserId,
        messageId: MessageId,
        receipt: Receipt
    ) {
        val currentReceipt = messageReceiptsRepository.findByUserIdAndMessageId(participantId, messageId)

        messageReceiptsRepository.save(
            currentReceipt.copy(receipt = receipt)
        )

        when (receipt) {
            Receipt.DELIVERED -> appEventPublisher.publishEvent(
                ChatEvent.OnDeliveredMessage(messageId)
            )

            Receipt.READ -> appEventPublisher.publishEvent(
                ChatEvent.OnReadMessage(messageId)
            )

            else -> {}
        }
    }

    private fun getDesiredChat(
        request: NewMessageRequest,
        sender: UserEntity
    ): ChatEntity = when {
        request.chatId.contains("__") -> {
            val receiver = userRepository.findById(request.receiver).get()

            chatRepository.findOneToOneChatForParticipants(sender, receiver) ?: let {
                val newChat = OneToOneChatEntity(originator = sender, participant = receiver)
                chatRepository.save(newChat)
            }
        }

        else -> {
            chatRepository.findById(request.receiver).get() as GroupChatEntity
        }
    }

    private fun getDesiredMessage(
        request: NewMessageRequest,
        sender: UserEntity,
        desiredChat: ChatEntity
    ): ChatMessageEntity = when (val message = request.message) {
        is MessageDto.TextMessageDto -> {
            TextMessageEntity(sender = sender, belongsToChat = desiredChat, text = message.text)
        }

        is MessageDto.ImageMessageDto -> {
            ImageMessageEntity(sender = sender, belongsToChat = desiredChat, imageUrl = message.imageUrl)
        }

        is MessageDto.VideoMessageDto -> {
            VideoMessageEntity(sender = sender, belongsToChat = desiredChat, videoUrl = message.videoUrl)
        }

        is MessageDto.AudioMessageDto -> {
            AudioMessageEntity(sender = sender, belongsToChat = desiredChat, audioUrl = message.audioUrl)
        }

        is MessageDto.DocumentMessageDto -> {
            DocumentMessageEntity(
                sender = sender,
                belongsToChat = desiredChat,
                documentUrl = message.documentUrl,
                documentName = message.documentName
            )
        }

        is MessageDto.ContactMessageDto -> TODO()
    }.also {
        messageRepository.save(it)
    }

    private fun getParticipantsForChat(chatId: ChatId, fromId: UserId): List<UserEntity> {
        return chatRepository.findById(chatId).get().participants
            .filter { it.id != fromId }
    }

}