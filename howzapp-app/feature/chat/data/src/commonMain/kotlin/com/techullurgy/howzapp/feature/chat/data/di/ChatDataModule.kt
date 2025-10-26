package com.techullurgy.howzapp.feature.chat.data.di

import com.techullurgy.howzapp.feature.chat.data.lifecycle.AppLifecycleObserver
import com.techullurgy.howzapp.feature.chat.data.networking.ConnectivityObserver
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
expect class ChatDataPlatformModule {
    @Single
    fun provideConnectivityObserver(scope: Scope): ConnectivityObserver

    @Single
    fun provideAppLifecycleObserver(scope: Scope): AppLifecycleObserver
}

@Module(includes = [ChatDataPlatformModule::class])
class ChatDataModule