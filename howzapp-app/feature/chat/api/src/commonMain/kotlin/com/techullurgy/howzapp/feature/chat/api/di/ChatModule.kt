package com.techullurgy.howzapp.feature.chat.api.di

import com.techullurgy.howzapp.feature.chat.data.di.ChatDataModule
import com.techullurgy.howzapp.feature.chat.data.networking.WebsocketJson
import com.techullurgy.howzapp.feature.chat.database.di.ChatDatabaseModule
import com.techullurgy.howzapp.feature.chat.domain.di.ChatTestModule
import com.techullurgy.howzapp.feature.chat.domain.networking.events.IncomingMessage
import com.techullurgy.howzapp.feature.chat.domain.networking.events.OutgoingMessage
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
                polymorphic(IncomingMessage::class) {
                    subclass(IncomingMessage.TypingMessage::class, IncomingMessage.TypingMessage.serializer())
                    subclass(IncomingMessage.RecordingAudioMessage::class, IncomingMessage.RecordingAudioMessage.serializer())
                    subclass(IncomingMessage.OnlineIndicatorMessage::class, IncomingMessage.OnlineIndicatorMessage.serializer())
                    subclass(IncomingMessage.OfflineIndicatorMessage::class, IncomingMessage.OfflineIndicatorMessage.serializer())
                    subclass(IncomingMessage.NotifyMessageSyncMessage::class, IncomingMessage.NotifyMessageSyncMessage.serializer())
                }

                polymorphic(OutgoingMessage::class) {
                    subclass(OutgoingMessage.TypingMessage::class, OutgoingMessage.TypingMessage.serializer())
                    subclass(OutgoingMessage.RecordingAudioMessage::class, OutgoingMessage.RecordingAudioMessage.serializer())
                    subclass(OutgoingMessage.ParticipantSubscriptionMessage::class, OutgoingMessage.ParticipantSubscriptionMessage.serializer())
                }
            }
        }
    }
}

@Module(includes = [ChatTestModule::class])
@Configuration("test")
class ChatModuleForTest