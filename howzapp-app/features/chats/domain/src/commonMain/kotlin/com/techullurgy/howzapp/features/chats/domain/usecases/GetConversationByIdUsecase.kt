package com.techullurgy.howzapp.features.chats.domain.usecases

import com.techullurgy.howzapp.features.chats.domain.repositories.ChatRepository
import com.techullurgy.howzapp.features.chats.models.Chat
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetConversationByIdUsecase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(
        conversationId: String
    ): Flow<Chat?> {
        return chatRepository.observeChatByChatId(conversationId)
    }
}