package com.techullurgy.howzapp.feature.chat.domain.usecases

import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import org.koin.core.annotation.Factory

@Factory
class MarkAsReadForMessageUsecase(
    private val chatLocalRepository: ChatLocalRepository
) {
    suspend operator fun invoke(messageId: String) {
        chatLocalRepository.markMessageAsRead(messageId)
    }
}