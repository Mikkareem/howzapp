package com.techullurgy.howzapp.feature.chat.data.di

import com.techullurgy.howzapp.feature.chat.data.networking.ConnectivityObserver
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
expect class ChatDataPlatformModule {
    @Single
    fun provideConnectivityObserver(scope: Scope): ConnectivityObserver
}

@Module(includes = [ChatDataPlatformModule::class])
@Configuration
class ChatDataModule