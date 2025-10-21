package com.techullurgy.howzapp.feature.chat.test.di

import com.techullurgy.howzapp.feature.chat.data.networking.ConnectivityObserver
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.dsl.module

val chatTestDomainModule = module(createdAtStart = true) {
    single<ConnectivityObserver> {
        println("Dependency used.....")
        mockk<ConnectivityObserver> {
            every { isConnected } returns MutableStateFlow(true)
        }
    }
}