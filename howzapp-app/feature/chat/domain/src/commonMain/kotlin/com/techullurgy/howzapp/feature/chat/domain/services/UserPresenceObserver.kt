package com.techullurgy.howzapp.feature.chat.domain.services

import com.techullurgy.howzapp.feature.chat.domain.networking.WebsocketConnector
import com.techullurgy.howzapp.feature.chat.domain.networking.events.IncomingMessage
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single
import kotlin.coroutines.coroutineContext

@Single(createdAtStart = true)
internal class UserPresenceObserver(
    connector: WebsocketConnector,
    applicationScope: CoroutineScope,
    repository: ChatLocalRepository,
) {
    init {
        merge(
            connector.messages.filterIsInstance<IncomingMessage.OnlineIndicatorMessage>(),
            connector.messages.filterIsInstance<IncomingMessage.OfflineIndicatorMessage>()
        )
            .onEach { status ->
                when(status) {
                    is IncomingMessage.OnlineIndicatorMessage -> {
                        try {
                            repository.updateUserAsOnline(status.userId)
                        } catch (_: Exception) {
                            coroutineContext.ensureActive()
                        }
                    }
                    is IncomingMessage.OfflineIndicatorMessage -> {
                        try {
                            repository.updateUserAsOffline(status.userId)
                        } catch (_: Exception) {
                            coroutineContext.ensureActive()
                        }
                    }
                    else -> throw RuntimeException("Not Possible Error")
                }
            }
            .launchIn(applicationScope)
    }
}