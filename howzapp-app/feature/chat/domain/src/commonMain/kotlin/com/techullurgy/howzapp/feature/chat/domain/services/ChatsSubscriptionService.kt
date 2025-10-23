package com.techullurgy.howzapp.feature.chat.domain.services

import com.techullurgy.howzapp.core.domain.logging.HowzappLogger
import com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState
import com.techullurgy.howzapp.feature.chat.domain.networking.WebsocketConnector
import com.techullurgy.howzapp.feature.chat.domain.networking.events.OutgoingMessage
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
internal class ChatsSubscriptionService(
    private val connector: WebsocketConnector,
    chatLocalRepository: ChatLocalRepository,
    appScope: CoroutineScope,
    private val logger: HowzappLogger
) {
    init {
        combine(
            connector.connectionState.map { it == ConnectionState.CONNECTED },
            chatLocalRepository.observeAllChats()
        ) { isConnected, chats ->
            if (isConnected) {
                chats.ifEmpty { null }
            } else null
        }
            .filterNotNull()
            .onEach { chats ->
                logger.info("Subscribing chats")
                connector.sendOutgoingMessage(
                    OutgoingMessage.ChatsSubscriptionMessage(chats = chats)
                )
            }
            .launchIn(appScope)
    }
}