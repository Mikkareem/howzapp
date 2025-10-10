package com.techullurgy.howzapp.feature.chat.data.repositories

import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class DefaultChatRepository(
    private val chatLocalRepository: ChatLocalRepository,
    private val chatNetworkRepository: ChatNetworkRepository
) : ChatRepository {
    override fun observeChatByChatId(
        chatId: String
    ): Flow<Chat?> {
        return chatLocalRepository.observeChatDetailsById(chatId)
    }
}