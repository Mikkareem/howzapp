package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    val connectionState: Flow<ConnectionState>

    fun observeChatByChatId(
        chatId: String
    ): Flow<Chat?>

    fun typingFor(chatId: String)
    fun recordingAudioFor(chatId: String)
}