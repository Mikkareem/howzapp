package com.techullurgy.howzapp.feature.chat.domain.usecases

import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
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