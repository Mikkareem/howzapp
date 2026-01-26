package com.techullurgy.howzapp.features.chats.domain.services

import com.techullurgy.howzapp.features.chats.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.features.chats.domain.websockets.ChatWebsocketConnector
import com.techullurgy.howzapp.features.chats.domain.websockets.events.ChatEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
internal class UserPresenceObserverService(
    connector: ChatWebsocketConnector,
    applicationScope: CoroutineScope,
    repository: ChatLocalRepository,
) {
    init {
        merge(
            connector.events.filterIsInstance<ChatEvent.IncomingEvent.OnlineIndicatorEvent>(),
            connector.events.filterIsInstance<ChatEvent.IncomingEvent.OfflineIndicatorEvent>()
        )
            .onEach { status ->
                when(status) {
                    is ChatEvent.IncomingEvent.OnlineIndicatorEvent -> {
                        try {
                            repository.updateUserOnlineStatus(status.userId, true)
                        } catch (_: Exception) {
                            currentCoroutineContext().ensureActive()
                        }
                    }

                    is ChatEvent.IncomingEvent.OfflineIndicatorEvent -> {
                        try {
                            repository.updateUserOnlineStatus(status.userId, false)
                        } catch (_: Exception) {
                            currentCoroutineContext().ensureActive()
                        }
                    }
                    else -> throw RuntimeException("Not Possible Error")
                }
            }
            .launchIn(applicationScope)
    }
}