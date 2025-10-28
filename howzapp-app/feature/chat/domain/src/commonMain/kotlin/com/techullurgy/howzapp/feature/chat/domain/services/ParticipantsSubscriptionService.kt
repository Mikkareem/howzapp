package com.techullurgy.howzapp.feature.chat.domain.services

import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.core.domain.logging.HowzappLogger
import com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState
import com.techullurgy.howzapp.feature.chat.domain.networking.WebsocketConnector
import com.techullurgy.howzapp.feature.chat.domain.networking.events.OutgoingMessage
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
internal class ParticipantsSubscriptionService(
    private val connector: WebsocketConnector,
    sessionStorage: SessionStorage,
    chatLocalRepository: ChatLocalRepository,
    appScope: CoroutineScope,
    private val logger: HowzappLogger
) {
    init {
        combine(
            connector.connectionState.map { it == ConnectionState.CONNECTED },
            sessionStorage.observeAuthInfo(),
            chatLocalRepository.observeAllParticipants()
        ) { isConnected, authInfo, participants ->
            authInfo?.let {
                if (isConnected) {
                    participants
                        .filter { p -> p != it.id }
                        .ifEmpty { null }
                } else null
            }
        }
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { participants ->
                logger.info("Subscribing Participants")
                connector.sendOutgoingMessage(
                    OutgoingMessage.ParticipantSubscriptionMessage(participants = participants)
                )
            }
            .launchIn(appScope)
    }
}