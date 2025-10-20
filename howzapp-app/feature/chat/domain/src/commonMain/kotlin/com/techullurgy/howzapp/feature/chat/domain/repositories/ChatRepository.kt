package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState
import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class ChatRepository {
    private val _events = MutableSharedFlow<UserChatEvent>()
    open val events: Flow<UserChatEvent> = _events.asSharedFlow()

    abstract val connectionState: Flow<ConnectionState>

    abstract fun observeChatByChatId(chatId: String): Flow<Chat?>

    abstract fun typingFor(chatId: String)
    abstract fun recordingAudioFor(chatId: String)

    internal fun newEvent(event: UserChatEvent) { _events.tryEmit(event) }
}