package com.techullurgy.howzapp.features.chats.domain.services

import com.techullurgy.howzapp.features.chats.domain.websockets.ChatWebsocketConnector
import com.techullurgy.howzapp.features.chats.domain.websockets.events.ChatEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class RecordingAudioIndicatorNotificationService internal constructor(
    private val connector: ChatWebsocketConnector,
    applicationScope: CoroutineScope
) {
    private val recordingAudioIndicatorNotificationChannel = Channel<String>(Channel.UNLIMITED)

    init {
        recordingAudioIndicatorNotificationChannel
            .receiveAsFlow()
            .onEach { chatId ->
                connector.sendEvent(ChatEvent.OutgoingEvent.RecordingAudioEvent(chatId))
            }
            .launchIn(applicationScope)
    }

    fun notify(chatId: String) {
        recordingAudioIndicatorNotificationChannel.trySend(chatId)
    }
}