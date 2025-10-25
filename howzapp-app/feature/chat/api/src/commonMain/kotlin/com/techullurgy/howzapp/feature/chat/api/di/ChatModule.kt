package com.techullurgy.howzapp.feature.chat.api.di

import com.techullurgy.howzapp.core.dto.websocket.WebsocketIncomingMessage
import com.techullurgy.howzapp.core.dto.websocket.WebsocketOutgoingMessage
import com.techullurgy.howzapp.feature.chat.data.di.ChatDataModule
import com.techullurgy.howzapp.feature.chat.data.networking.WebsocketJson
import com.techullurgy.howzapp.feature.chat.database.di.ChatDatabaseModule
import com.techullurgy.howzapp.feature.chat.database.di.ChatDatabaseTestModule
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module(includes = [ChatDatabaseModule::class, ChatDataModule::class])
@Configuration
@ComponentScan("com.techullurgy.howzapp.feature.chat")
class ChatModule {
    @Single
    @WebsocketJson
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true

            serializersModule = SerializersModule {
                polymorphic(WebsocketIncomingMessage::class) {
                    subclass(
                        WebsocketIncomingMessage.TypingMessage::class,
                        WebsocketIncomingMessage.TypingMessage.serializer()
                    )
                    subclass(
                        WebsocketIncomingMessage.RecordingAudioMessage::class,
                        WebsocketIncomingMessage.RecordingAudioMessage.serializer()
                    )
                    subclass(
                        WebsocketIncomingMessage.OnlineIndicatorMessage::class,
                        WebsocketIncomingMessage.OnlineIndicatorMessage.serializer()
                    )
                    subclass(
                        WebsocketIncomingMessage.OfflineIndicatorMessage::class,
                        WebsocketIncomingMessage.OfflineIndicatorMessage.serializer()
                    )
                    subclass(
                        WebsocketIncomingMessage.NotifyMessageSyncMessage::class,
                        WebsocketIncomingMessage.NotifyMessageSyncMessage.serializer()
                    )
                }

                polymorphic(WebsocketOutgoingMessage::class) {
                    subclass(
                        WebsocketOutgoingMessage.TypingMessage::class,
                        WebsocketOutgoingMessage.TypingMessage.serializer()
                    )
                    subclass(
                        WebsocketOutgoingMessage.RecordingAudioMessage::class,
                        WebsocketOutgoingMessage.RecordingAudioMessage.serializer()
                    )
                    subclass(
                        WebsocketOutgoingMessage.ParticipantSubscriptionMessage::class,
                        WebsocketOutgoingMessage.ParticipantSubscriptionMessage.serializer()
                    )
                    subclass(
                        WebsocketOutgoingMessage.ChatsSubscriptionMessage::class,
                        WebsocketOutgoingMessage.ChatsSubscriptionMessage.serializer()
                    )
                }
            }
        }
    }
}

@Module(
    includes = [
//        ChatTestModule::class,
        ChatDatabaseTestModule::class
    ]
)
@Configuration("test")
class ChatModuleForTest