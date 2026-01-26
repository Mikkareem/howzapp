package com.techullurgy.howzapp.features.chats.domain.usecases

import com.techullurgy.howzapp.features.chats.domain.repositories.ChatLocalRepository
import org.koin.core.annotation.Factory

@Factory
class MarkAsReadForMessageUsecase(
    private val chatLocalRepository: ChatLocalRepository
) {
    suspend operator fun invoke(messageId: String) {
        chatLocalRepository.markMessageAsRead(messageId)
    }
}