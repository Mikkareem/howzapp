package com.techullurgy.howzapp.chats.services.usecases

import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageRepository
import com.techullurgy.howzapp.chats.infra.mappers.toDomain
import com.techullurgy.howzapp.chats.models.ChatMessage
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.UserId
import org.springframework.stereotype.Component

@Component
class LoadMessagesByIdsUsecase(
    private val messageRepository: ChatMessageRepository
) {
    operator fun invoke(
        requester: UserId,
        messageIds: List<MessageId>
    ): List<ChatMessage> {
        return messageIds.mapNotNull {
            messageRepository.getMessageByIdForRequester(requester, it)?.toDomain()
        }
    }
}