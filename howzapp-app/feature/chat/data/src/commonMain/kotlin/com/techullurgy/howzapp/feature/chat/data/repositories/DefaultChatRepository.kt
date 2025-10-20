package com.techullurgy.howzapp.feature.chat.data.repositories

import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState
import com.techullurgy.howzapp.feature.chat.domain.networking.WebsocketConnector
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
import com.techullurgy.howzapp.feature.chat.domain.services.RecordingAudioIndicationNotifier
import com.techullurgy.howzapp.feature.chat.domain.services.TypingIndicationNotifier
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class DefaultChatRepository(
    private val chatLocalRepository: ChatLocalRepository,
    private val chatNetworkRepository: ChatNetworkRepository,
    private val typingNotifier: TypingIndicationNotifier,
    private val recordingAudioNotifier: RecordingAudioIndicationNotifier,
    connector: WebsocketConnector
) : ChatRepository() {

    override val connectionState: Flow<ConnectionState> = connector.connectionState

    override fun observeChatByChatId(
        chatId: String
    ): Flow<Chat?> {
        return chatLocalRepository.observeConversation(chatId)
    }

    override fun typingFor(chatId: String) {
        typingNotifier.notify(chatId)
    }

    override fun recordingAudioFor(chatId: String) {
        recordingAudioNotifier.notify(chatId)
    }
}