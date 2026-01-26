package com.techullurgy.howzapp.features.chats.domain.services

import com.techullurgy.howzapp.common.models.ConnectionState
import com.techullurgy.howzapp.core.session.SessionNotifier
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.features.chats.domain.websockets.ChatWebsocketConnector
import com.techullurgy.howzapp.features.chats.domain.websockets.events.ChatEvent
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
    private val connector: ChatWebsocketConnector,
    sessionNotifier: SessionNotifier,
    chatLocalRepository: ChatLocalRepository,
    appScope: CoroutineScope,
//    private val logger: HowzappLogger
) {
    init {
        combine(
            connector.connectionState.map { it == ConnectionState.CONNECTED },
            sessionNotifier.observeSessionInfo(),
            chatLocalRepository.observeAllParticipants()
        ) { isConnected, sessionInfo, participants ->
            sessionInfo?.let {
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
//                logger.info("Subscribing Participants")
                connector.sendEvent(
                    ChatEvent.OutgoingEvent.ParticipantSubscriptionEvent(participants = participants)
                )
            }
            .launchIn(appScope)
    }
}