package com.techullurgy.howzapp.chats.services.usecases

import com.techullurgy.howzapp.chats.api.dto.MessageDto
import com.techullurgy.howzapp.chats.events.ChatEvent
import com.techullurgy.howzapp.chats.infra.database.entities.*
import com.techullurgy.howzapp.chats.infra.database.entities.chats.GroupChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.chats.OneToOneChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.messages.*
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageReceiptsRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageStatusRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatRepository
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import com.techullurgy.howzapp.users.infra.database.repositories.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import kotlin.uuid.Uuid

@Component
class NewMessageUsecase(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: ChatMessageRepository,
    private val statusRepository: ChatMessageStatusRepository,
    private val receiptsRepository: ChatMessageReceiptsRepository,
    private val appEventPublisher: ApplicationEventPublisher,

    private val getParticipantsForChatExceptSenderUsecase: GetParticipantsForChatExceptSenderUsecase
) {
    operator fun invoke(
        from: UserId,
        chatId: ChatId,
        message: MessageDto
    ): MessageId {
        val sender = userRepository.findById(from).get()

        val desiredChat = getDesiredChat(chatId, sender)

        val messageEntity = getDesiredMessage(message, sender, desiredChat)

        ChatMessageStatusEntity(
            message = messageEntity,
            sender = sender,
            status = MessageStatus.SENT
        ).also {
            statusRepository.save(it)
        }

        getParticipantsForChatExceptSender(desiredChat.id, sender.id).run {
            forEach { participant ->
                ChatMessageReceiptsEntity(
                    id = Uuid.id.toString(),
                    message = messageEntity,
                    user = participant,
                    receipt = Receipt.PENDING
                ).also {
                    receiptsRepository.save(it)
                }
            }

            forEach {
                appEventPublisher.publishEvent(
                    ChatEvent.OnNewMessage(it.id)
                )
            }
        }

        return messageEntity.id
    }

    private fun getDesiredChat(
        chatId: ChatId,
        sender: UserEntity
    ): ChatEntity = when {
        chatId.contains("__") -> {
            assert(chatId.split("__").contains(sender.id))

            val receiverId = chatId.split("__").first { it != sender.id }
            val receiver = userRepository.findById(receiverId).get()

            assert(chatId.split("__").contains(receiver.id))

            chatRepository.findOneToOneChatForParticipants(sender, receiver) ?: let {
                val newChat = OneToOneChatEntity(id = chatId, originator = sender, participant = receiver)
                chatRepository.save(newChat)
            }
        }

        else -> {
            chatRepository.findById(chatId).get() as GroupChatEntity
        }
    }

    private fun getDesiredMessage(
        message: MessageDto,
        sender: UserEntity,
        desiredChat: ChatEntity
    ): ChatMessageEntity = when (message) {
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
                documentName = message.documentName,
            )
        }

        is MessageDto.ContactMessageDto -> TODO()
    }.also {
        messageRepository.save(it)
    }

    private fun getParticipantsForChatExceptSender(chatId: ChatId, fromId: UserId): List<UserEntity> {
        return getParticipantsForChatExceptSenderUsecase(chatId, fromId)
    }
}