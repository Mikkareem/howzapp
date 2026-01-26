package com.techullurgy.howzapp.features.chats.domain.usecases

import com.techullurgy.howzapp.features.chats.domain.repositories.ChatRepository
import com.techullurgy.howzapp.features.chats.models.ChatPreview
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetChatPreviewsUsecase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<ChatPreview>> {
        return chatRepository.observeChatPreviews()
    }
}