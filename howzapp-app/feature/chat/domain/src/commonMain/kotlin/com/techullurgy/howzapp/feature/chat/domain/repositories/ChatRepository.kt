package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatPreview
import com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState
import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface ChatRepository {
    val connectionState: Flow<ConnectionState>

    fun observeChatPreviews(): Flow<List<ChatPreview>>
    fun observeChatByChatId(chatId: String): Flow<Chat?>

    fun typingFor(chatId: String)
    fun recordingAudioFor(chatId: String)
}