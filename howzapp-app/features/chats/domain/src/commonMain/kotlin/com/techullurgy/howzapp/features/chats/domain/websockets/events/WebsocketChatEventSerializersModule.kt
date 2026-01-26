package com.techullurgy.howzapp.features.chats.domain.websockets.events

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

internal object WebsocketChatEventSerializersModule {
    val module: SerializersModule = SerializersModule {
        polymorphic(WebsocketChatEvent.IncomingEvent::class) {
            subclass(
                WebsocketChatEvent.IncomingEvent.TypingEvent::class,
                WebsocketChatEvent.IncomingEvent.TypingEvent.serializer()
            )

            subclass(
                WebsocketChatEvent.IncomingEvent.RecordingAudioEvent::class,
                WebsocketChatEvent.IncomingEvent.RecordingAudioEvent.serializer()
            )

            subclass(
                WebsocketChatEvent.IncomingEvent.OnlineIndicatorEvent::class,
                WebsocketChatEvent.IncomingEvent.OnlineIndicatorEvent.serializer()
            )

            subclass(
                WebsocketChatEvent.IncomingEvent.OfflineIndicatorEvent::class,
                WebsocketChatEvent.IncomingEvent.OfflineIndicatorEvent.serializer()
            )

            subclass(
                WebsocketChatEvent.IncomingEvent.NotifyMessageSyncEvent::class,
                WebsocketChatEvent.IncomingEvent.NotifyMessageSyncEvent.serializer()
            )
        }

        polymorphic(WebsocketChatEvent.OutgoingEvent::class) {
            subclass(
                WebsocketChatEvent.OutgoingEvent.TypingEvent::class,
                WebsocketChatEvent.OutgoingEvent.TypingEvent.serializer()
            )

            subclass(
                WebsocketChatEvent.OutgoingEvent.RecordingAudioEvent::class,
                WebsocketChatEvent.OutgoingEvent.RecordingAudioEvent.serializer()
            )

            subclass(
                WebsocketChatEvent.OutgoingEvent.ParticipantSubscriptionEvent::class,
                WebsocketChatEvent.OutgoingEvent.ParticipantSubscriptionEvent.serializer()
            )

            subclass(
                WebsocketChatEvent.OutgoingEvent.ChatsSubscriptionEvent::class,
                WebsocketChatEvent.OutgoingEvent.ChatsSubscriptionEvent.serializer()
            )
        }
    }
}