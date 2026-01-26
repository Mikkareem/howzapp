package com.techullurgy.howzapp.features.chats.data.repositories

import com.techullurgy.howzapp.common.models.ConnectionState
import com.techullurgy.howzapp.core.data.api.AppWebsocketConnector
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatRepository
import com.techullurgy.howzapp.features.chats.domain.services.RecordingAudioIndicatorNotificationService
import com.techullurgy.howzapp.features.chats.domain.services.TypingIndicatorNotificationService
import com.techullurgy.howzapp.features.chats.models.Chat
import com.techullurgy.howzapp.features.chats.models.ChatPreview
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
internal class DefaultChatRepository(
    private val chatLocalRepository: ChatLocalRepository,
    private val typingNotifier: TypingIndicatorNotificationService,
    private val recordingAudioNotifier: RecordingAudioIndicatorNotificationService,
    connector: AppWebsocketConnector
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