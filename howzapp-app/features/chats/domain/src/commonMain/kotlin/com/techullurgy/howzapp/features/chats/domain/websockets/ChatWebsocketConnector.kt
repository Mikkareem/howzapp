package com.techullurgy.howzapp.features.chats.domain.websockets

import com.techullurgy.howzapp.common.models.ConnectionState
import com.techullurgy.howzapp.core.data.api.AppWebsocketConnector
import com.techullurgy.howzapp.features.chats.domain.websockets.events.ChatEvent
import com.techullurgy.howzapp.features.chats.domain.websockets.events.WebsocketChatEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transform
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
internal class ChatWebsocketConnector(
    scope: CoroutineScope,
    @get:ChatWebsocketJson private val json: Json,
    private val connector: AppWebsocketConnector
) {
    val connectionState: StateFlow<ConnectionState> = connector.connectionState

    val events: SharedFlow<ChatEvent.IncomingEvent> = connector.messages
        .transform {
            try {
                val websocketEvent = json.decodeFromString<WebsocketChatEvent.IncomingEvent>(it)
                emit(websocketEvent)
            } catch (_: SerializationException) {
            }
        }
        .map {
            when (it) {
                WebsocketChatEvent.IncomingEvent.NotifyMessageSyncEvent -> ChatEvent.IncomingEvent.NotifyMessageSyncEvent
                is WebsocketChatEvent.IncomingEvent.OfflineIndicatorEvent -> ChatEvent.IncomingEvent.OfflineIndicatorEvent(
                    it.userId
                )

                is WebsocketChatEvent.IncomingEvent.OnlineIndicatorEvent -> ChatEvent.IncomingEvent.OnlineIndicatorEvent(
                    it.userId
                )

                is WebsocketChatEvent.IncomingEvent.RecordingAudioEvent -> ChatEvent.IncomingEvent.RecordingAudioEvent(
                    it.chatId,
                    it.userId
                )

                is WebsocketChatEvent.IncomingEvent.TypingEvent -> ChatEvent.IncomingEvent.TypingEvent(
                    it.chatId,
                    it.userId
                )
            }
        }
        .shareIn(
            scope = scope,
            started = SharingStarted.Eagerly
        )

    suspend fun sendEvent(event: ChatEvent.OutgoingEvent) {
        val outgoingEvent = when (event) {
            is ChatEvent.OutgoingEvent.ChatsSubscriptionEvent -> WebsocketChatEvent.OutgoingEvent.ChatsSubscriptionEvent(
                event.chats
            )

            is ChatEvent.OutgoingEvent.ParticipantSubscriptionEvent -> WebsocketChatEvent.OutgoingEvent.ParticipantSubscriptionEvent(
                event.participants
            )

            is ChatEvent.OutgoingEvent.RecordingAudioEvent -> WebsocketChatEvent.OutgoingEvent.RecordingAudioEvent(
                event.chatId
            )

            is ChatEvent.OutgoingEvent.TypingEvent -> WebsocketChatEvent.OutgoingEvent.TypingEvent(
                event.chatId
            )
        }
        val message = json.encodeToString<WebsocketChatEvent.OutgoingEvent>(outgoingEvent)
        connector.sendOutgoingMessage(message)
    }
}