package com.techullurgy.howzapp.chats.services.usecases

import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageRepository
import com.techullurgy.howzapp.chats.infra.mappers.toDomain
import com.techullurgy.howzapp.chats.models.ChatMessage
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.UserId
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class LoadMessageFromChatBeforeMessageUsecase(
    private val messageRepository: ChatMessageRepository,
) {
    operator fun invoke(
        user: UserId,
        chatId: ChatId,
        lastMessage: MessageId
    ): List<ChatMessage> {
        return messageRepository.loadChunkOfMessagesFromChat(
            requester = user,
            chat = chatId,
            beforeToMessage = lastMessage,
            pageable = PageRequest.of(0, 20)
        )
            .content
            .map { it.toDomain() }
    }
}