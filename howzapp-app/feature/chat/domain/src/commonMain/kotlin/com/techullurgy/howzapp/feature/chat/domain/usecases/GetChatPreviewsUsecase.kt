package com.techullurgy.howzapp.feature.chat.domain.usecases

import com.techullurgy.howzapp.feature.chat.domain.models.ChatPreview
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
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