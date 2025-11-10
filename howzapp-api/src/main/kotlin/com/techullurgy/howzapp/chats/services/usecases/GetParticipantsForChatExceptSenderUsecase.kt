package com.techullurgy.howzapp.chats.services.usecases

import com.techullurgy.howzapp.chats.infra.database.repositories.ChatRepository
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import org.springframework.stereotype.Component

@Component
class GetParticipantsForChatExceptSenderUsecase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(
        chatId: ChatId,
        fromId: UserId,
    ): List<UserEntity> {
        return chatRepository.findById(chatId).get().participants.filter { it.id != fromId }
    }
}