package com.techullurgy.howzapp.features.chats.domain.repositories

import com.techullurgy.howzapp.common.models.ConnectionState
import com.techullurgy.howzapp.features.chats.models.Chat
import com.techullurgy.howzapp.features.chats.models.ChatPreview
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val connectionState: Flow<ConnectionState>

    fun observeChatPreviews(): Flow<List<ChatPreview>>
    fun observeChatByChatId(chatId: String): Flow<Chat?>

    fun typingFor(chatId: String)
    fun recordingAudioFor(chatId: String)
}