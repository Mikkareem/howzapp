package com.techullurgy.howzapp.features.chats.domain.services

import com.techullurgy.howzapp.features.chats.domain.websockets.ChatWebsocketConnector
import com.techullurgy.howzapp.features.chats.domain.websockets.events.ChatEvent
import com.techullurgy.howzapp.features.chats.models.UserChatEvent
import com.techullurgy.howzapp.features.chats.models.UserChatEventType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import kotlin.collections.emptyList

@Single(createdAtStart = true)
internal class UserChatEventsTrackerService(
    connector: ChatWebsocketConnector,
    appScope: CoroutineScope
) {
    private val _events = MutableStateFlow<List<UserChatEvent>>(emptyList())
    internal val events = _events
        .map { it.reversed() }
        .stateIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _events.value
        )

    private val _incomingEvents = Channel<UserChatEvent>(Channel.CONFLATED)

    init {
        with(appScope) {
            observeEvents()
            observeNecessaryIncomingMessages(connector)
        }
    }

    private fun CoroutineScope.observeNecessaryIncomingMessages(
        connector: ChatWebsocketConnector,
    ) {
        merge(
            connector.events.filterIsInstance<ChatEvent.IncomingEvent.TypingEvent>(),
            connector.events.filterIsInstance<ChatEvent.IncomingEvent.RecordingAudioEvent>()
        )
            .onEach {
                when(it) {
                    is ChatEvent.IncomingEvent.RecordingAudioEvent -> {
                        _incomingEvents.trySend(
                            UserChatEvent(
                                chatId = it.chatId,
                                userId = it.userId,
                                eventType = UserChatEventType.RECORDING_AUDIO
                            )
                        )
                    }

                    is ChatEvent.IncomingEvent.TypingEvent -> {
                        _incomingEvents.trySend(
                            UserChatEvent(
                                chatId = it.chatId,
                                userId = it.userId,
                                eventType = UserChatEventType.TYPING
                            )
                        )
                    }
                    else -> TODO()
                }
            }
            .launchIn(this)
    }

    private fun CoroutineScope.observeEvents() {
        _incomingEvents
            .receiveAsFlow()
            .onEach {
                _events.value = _events.value + it
            }
            .timeout(3000) {
                _events.value = _events.value - it
            }
            .launchIn(this)
    }
}

private fun <T> Flow<T>.timeout(millis: Long, onTimeoutForIdentifier: (T) -> Unit): Flow<T> {
    return callbackFlow {
        collect {
            send(it)
            launch {
                delay(millis)
                onTimeoutForIdentifier(it)
            }
        }
    }
}