package com.techullurgy.howzapp.chats.services

import com.techullurgy.howzapp.chats.api.MessageDto
import com.techullurgy.howzapp.chats.api.NewMessageRequest
import com.techullurgy.howzapp.chats.api.ReceiverType
import com.techullurgy.howzapp.chats.events.ChatEvent
import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageStatusEntity
import com.techullurgy.howzapp.chats.infra.database.entities.MessageStatus
import com.techullurgy.howzapp.chats.infra.database.entities.chats.GroupChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.chats.OneToOneChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.messages.*
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageStatusRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatRepository
import com.techullurgy.howzapp.chats.infra.mappers.toDomain
import com.techullurgy.howzapp.chats.models.*
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.users.infra.database.repositories.UserRepository
import com.techullurgy.howzapp.users.infra.mappers.toDomain
import com.techullurgy.howzapp.users.models.AppUser
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val messageRepository: ChatMessageRepository,
    private val messageStatusRepository: ChatMessageStatusRepository,
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
    fun getNotifiableParticipantsForChat(chatId: ChatId, fromId: UserId): List<AppUser> {
        return chatRepository.findById(chatId).get().participants
            .filter { it.id != fromId }
            .map { it.toDomain() }
    }

    @Transactional
    fun onNewMessage(request: NewMessageRequest) {
        val sender = UserId.parse(request.sender).let {
            userRepository.findById(it).get()
        }

        val desiredChat = when (request.receiverType) {
            ReceiverType.ONE_TO_ONE_CHAT -> {
                val receiver = UserId.parse(request.receiver).let {
                    userRepository.findById(it).get()
                }

                chatRepository.findOneToOneChatForParticipants(sender, receiver) ?: let {
                    val newChat = OneToOneChatEntity(originator = sender, participant = receiver)
                    chatRepository.save(newChat)
                }
            }
            ReceiverType.GROUP_CHAT -> {
                ChatId.parse(request.receiver).let {
                    chatRepository.findById(it).get() as GroupChatEntity
                }
            }
        }

        val messageEntity = when (val message = request.message) {
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
                DocumentMessageEntity(sender = sender, belongsToChat = desiredChat, documentUrl = message.documentUrl, documentName = message.documentName)
            }
            is MessageDto.ContactMessageDto -> TODO()
        }

        messageRepository.save(messageEntity)

        val statusEntity = ChatMessageStatusEntity(
            message = messageEntity, sender = sender, status = MessageStatus.SENT
        )
        messageStatusRepository.save(statusEntity)

        appEventPublisher.publishEvent(
            ChatEvent.OnNewMessage(messageEntity.id, desiredChat.id, sender.id)
        )
    }
}