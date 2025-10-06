package com.techullurgy.howzapp.feature.chat.data.di

import com.techullurgy.howzapp.feature.chat.data.networking.ConnectionRetryHandler
import com.techullurgy.howzapp.feature.chat.data.networking.KtorWebsocketConnector
import com.techullurgy.howzapp.feature.chat.data.repositories.DefaultChatLocalRepository
import com.techullurgy.howzapp.feature.chat.data.repositories.DefaultChatNetworkRepository
import com.techullurgy.howzapp.feature.chat.data.services.DefaultChatSynchronizer
import com.techullurgy.howzapp.feature.chat.database.di.chatDatabaseModule
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import com.techullurgy.howzapp.feature.chat.domain.services.ChatSynchronizer
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect val platformModule: Module

val chatDataModule = module {
    includes(chatDatabaseModule, platformModule)
    singleOf(::DefaultChatNetworkRepository) bind ChatNetworkRepository::class
    singleOf(::DefaultChatLocalRepository) bind ChatLocalRepository::class
    singleOf(::DefaultChatSynchronizer) bind ChatSynchronizer::class
    singleOf(::ConnectionRetryHandler)
    singleOf(::KtorWebsocketConnector)

    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
    }
}