package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeChatByChatId(
        chatId: String
    ): Flow<Chat?>
}