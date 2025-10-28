package com.techullurgy.howzapp.feature.chat.data.di

import com.techullurgy.howzapp.feature.chat.data.lifecycle.AppLifecycleObserver
import com.techullurgy.howzapp.feature.chat.data.networking.ConnectivityObserver
import com.techullurgy.howzapp.feature.chat.domain.system.FileReader
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
expect class ChatDataPlatformModule {
    @Single
    fun provideConnectivityObserver(scope: Scope): ConnectivityObserver

    @Single
    fun provideAppLifecycleObserver(scope: Scope): AppLifecycleObserver

    @Single
    fun provideFileReader(scope: Scope): FileReader
}

@Module(includes = [ChatDataPlatformModule::class])
class ChatDataModule