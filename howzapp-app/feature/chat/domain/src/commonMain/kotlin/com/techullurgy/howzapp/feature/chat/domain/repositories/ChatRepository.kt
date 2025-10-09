package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val chatLocalRepository: ChatLocalRepository,
    private val chatNetworkRepository: ChatNetworkRepository
) {
    fun observeChatByChatId(
        chatId: String
    ): Flow<Chat?> {
        return chatLocalRepository.observeChatDetailsById(chatId)
    }
}