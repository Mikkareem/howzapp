package com.techullurgy.howzapp.feature.chat.test.di

import com.techullurgy.howzapp.feature.chat.data.networking.ConnectivityObserver
import com.techullurgy.howzapp.feature.chat.data.repositories.DefaultChatLocalRepository
import com.techullurgy.howzapp.feature.chat.data.repositories.DefaultChatNetworkRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import com.techullurgy.howzapp.feature.chat.test.utils.ConnectivityNotifier
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.koin.dsl.module

val chatTestDomainModule = module(createdAtStart = true) {

    single { ConnectivityNotifier() }

    single<ConnectivityObserver> {
        mockk<ConnectivityObserver> {
            every { isConnected } returns get<ConnectivityNotifier>().flow
        }
    }

    single<ChatNetworkRepository> { spyk(DefaultChatNetworkRepository(get())) }
    single<ChatLocalRepository> { spyk(DefaultChatLocalRepository(get())) }
}