package com.techullurgy.howzapp.feature.chat.data.di

import android.content.Context
import com.techullurgy.howzapp.feature.chat.data.networking.ConnectivityObserver
import com.techullurgy.howzapp.feature.chat.data.networking.PlatformConnectivityObserver
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
actual class ChatDataPlatformModule {
    @Single
    actual fun provideConnectivityObserver(scope: Scope): ConnectivityObserver {
        return PlatformConnectivityObserver(scope.get<Context>())
    }
}