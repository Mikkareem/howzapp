package com.techullurgy.howzapp.features.chats.domain.services

import com.techullurgy.howzapp.common.utils.models.onSuccess
import com.techullurgy.howzapp.core.session.SessionNotifier
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatNetworkRepository
import com.techullurgy.howzapp.features.chats.domain.websockets.ChatWebsocketConnector
import com.techullurgy.howzapp.features.chats.domain.websockets.events.ChatEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
internal class ChatsSynchronizationService(
    appScope: CoroutineScope,
    private val connector: ChatWebsocketConnector,
    private val sessionNotifier: SessionNotifier,
    private val chatLocalRepository: ChatLocalRepository,
    private val chatNetworkRepository: ChatNetworkRepository
) {

    init {
        with(appScope) {
            observeMessageSyncToken()
            observeLoginLogoutCase()
        }
    }

    private fun CoroutineScope.observeLoginLogoutCase() {
        combine(
            sessionNotifier.observeSessionInfo(),
            sessionNotifier.observeLastSyncTimestamp()
        ) { sessionInfo, timestamp ->
            (timestamp == 0L).takeIf { it }?.let {
                if (sessionInfo != null)
                    LoginLogoutCase.Login
                else LoginLogoutCase.Logout
            }
        }
            .filterNotNull()
            .onEach {
                when(it) {
                    LoginLogoutCase.Login -> {
                        handleMessageSyncToken()
                    }
                    LoginLogoutCase.Logout -> {
                        chatLocalRepository.reset()
                    }
                }
            }
            .launchIn(this)
    }

    private fun CoroutineScope.observeMessageSyncToken() {
        connector.events
            .filterIsInstance<ChatEvent.IncomingEvent.NotifyMessageSyncEvent>()
            .debounce(3000)
            .onEach {
                handleMessageSyncToken()
            }
            .launchIn(this)
    }

    private suspend fun handleMessageSyncToken() {
        val currentLastSyncTimeStamp =
            sessionNotifier.observeLastSyncTimestamp().firstOrNull() ?: return

        chatNetworkRepository.syncChats(currentLastSyncTimeStamp)
            .onSuccess { result ->
                chatLocalRepository.syncChats(result.chats)
                    .onSuccess {
                        sessionNotifier.setLastSyncTimestamp(result.lastSyncTimestamp)
                    }
            }
    }
}

private sealed interface LoginLogoutCase {
    data object Login: LoginLogoutCase
    data object Logout: LoginLogoutCase
}