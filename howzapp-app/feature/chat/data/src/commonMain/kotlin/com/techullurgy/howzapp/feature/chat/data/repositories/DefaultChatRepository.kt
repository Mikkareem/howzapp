package com.techullurgy.howzapp.feature.chat.data.repositories

import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatPreview
import com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState
import com.techullurgy.howzapp.feature.chat.domain.networking.WebsocketConnector
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
import com.techullurgy.howzapp.feature.chat.domain.services.RecordingAudioIndicationNotifierService
import com.techullurgy.howzapp.feature.chat.domain.services.TypingIndicationNotifierService
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class DefaultChatRepository(
    private val chatLocalRepository: ChatLocalRepository,
    private val typingNotifier: TypingIndicationNotifierService,
    private val recordingAudioNotifier: RecordingAudioIndicationNotifierService,
    connector: WebsocketConnector
) : ChatRepository {

    override val connectionState: Flow<ConnectionState> = connector.connectionState

    override fun observeChatPreviews(): Flow<List<ChatPreview>> {
        return chatLocalRepository.observeChatWithLastMessageAndUnreadCount()
    }

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