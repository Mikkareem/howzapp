package com.techullurgy.howzapp.feature.chat.domain.services

import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.core.domain.util.onSuccess
import com.techullurgy.howzapp.feature.chat.domain.networking.WebsocketConnector
import com.techullurgy.howzapp.feature.chat.domain.networking.events.IncomingMessage
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
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
class ChatsSynchronizationService(
    appScope: CoroutineScope,
    private val connector: WebsocketConnector,
    private val sessionStorage: SessionStorage,
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
            sessionStorage.observeAuthInfo(),
            sessionStorage.observeLastSyncTimestamp()
        ) { authInfo, timestamp ->
            (timestamp == 0L).takeIf { it }?.let {
                if(authInfo != null)
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
        connector.messages
            .filterIsInstance<IncomingMessage.NotifyMessageSyncMessage>()
            .debounce(3000)
            .onEach {
                handleMessageSyncToken()
            }
            .launchIn(this)
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

private sealed interface LoginLogoutCase {
    data object Login: LoginLogoutCase
    data object Logout: LoginLogoutCase
}