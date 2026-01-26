package com.techullurgy.howzapp.features.chats.domain.di

import com.techullurgy.howzapp.features.chats.domain.websockets.ChatWebsocketJson
import com.techullurgy.howzapp.features.chats.domain.websockets.events.WebsocketChatEventSerializersModule
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.techullurgy.howzapp.features.chats.domain")
class ChatDomainModule {

    @Single
    @ChatWebsocketJson
    internal fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            serializersModule = WebsocketChatEventSerializersModule.module
        }
    }
}