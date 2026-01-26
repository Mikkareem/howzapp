package com.techullurgy.howzapp.features.chats.domain.usecases

import com.techullurgy.howzapp.features.chats.domain.repositories.ChatRepository
import org.koin.core.annotation.Factory

@Factory
class TypingNotifierUsecase(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(chatid: String) {
        chatRepository.typingFor(chatid)
    }
}