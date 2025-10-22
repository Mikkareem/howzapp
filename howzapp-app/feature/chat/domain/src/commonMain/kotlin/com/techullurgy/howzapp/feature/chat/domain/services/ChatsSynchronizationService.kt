package com.techullurgy.howzapp.feature.chat.domain.services

import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.core.domain.util.onSuccess
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.networking.WebsocketConnector
import com.techullurgy.howzapp.feature.chat.domain.networking.events.IncomingMessage
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.supervisorScope
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class ChatsSynchronizationService(
    connector: WebsocketConnector,
    applicationScope: CoroutineScope,
    private val sessionStorage: SessionStorage,
    private val chatLocalRepository: ChatLocalRepository,
    private val chatNetworkRepository: ChatNetworkRepository
) {

    init {
        connector.messages
            .filterIsInstance<IncomingMessage.NotifyMessageSyncMessage>()
            .debounce(3000)
            .onEach {
                handleMessageSyncToken()
            }
            .launchIn(applicationScope)
    }

    private suspend fun handleMessageSyncToken() {
        val currentLastSyncTimeStamp = sessionStorage.observeLastSyncTimestamp().firstOrNull() ?: return

        chatNetworkRepository.syncChats(currentLastSyncTimeStamp)
            .onSuccess { result ->
                chatLocalRepository.syncChats(result.chats)
                    .onSuccess {
                        sessionStorage.setLastSyncTimestamp(result.lastSyncTimestamp)
                    }
            }
    }
}