package com.techullurgy.howzapp.feature.chat.domain.services

import com.techullurgy.howzapp.feature.chat.domain.networking.WebsocketConnector
import com.techullurgy.howzapp.feature.chat.domain.networking.events.OutgoingMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class RecordingAudioIndicationNotifierService(
    private val connector: WebsocketConnector,
    applicationScope: CoroutineScope
) {
    private val recordingAudioIndicatorNotificationChannel = Channel<String>(Channel.UNLIMITED)

    init {
        recordingAudioIndicatorNotificationChannel
            .receiveAsFlow()
            .onEach { chatId ->
                connector.sendOutgoingMessage(OutgoingMessage.RecordingAudioMessage(chatId))
            }
            .launchIn(applicationScope)
    }

    fun notify(chatId: String) {
        recordingAudioIndicatorNotificationChannel.trySend(chatId)
    }
}