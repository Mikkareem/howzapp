package com.techullurgy.howzapp.feature.chat.data.di

import com.techullurgy.howzapp.feature.chat.data.lifecycle.AppLifecycleObserver
import com.techullurgy.howzapp.feature.chat.data.lifecycle.PlatformAppLifecycleObserver
import com.techullurgy.howzapp.feature.chat.data.networking.ConnectivityObserver
import com.techullurgy.howzapp.feature.chat.data.networking.PlatformConnectivityObserver
import com.techullurgy.howzapp.feature.chat.data.system.FileReaderImpl
import com.techullurgy.howzapp.feature.chat.domain.system.FileReader
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
actual class ChatDataPlatformModule {
    @Single
    actual fun provideConnectivityObserver(scope: Scope): ConnectivityObserver {
        return PlatformConnectivityObserver()
    }

    @Single
    actual fun provideAppLifecycleObserver(scope: Scope): AppLifecycleObserver {
        return PlatformAppLifecycleObserver()
    }

    @Single
    actual fun provideFileReader(scope: Scope): FileReader {
        return FileReaderImpl()
    }
}