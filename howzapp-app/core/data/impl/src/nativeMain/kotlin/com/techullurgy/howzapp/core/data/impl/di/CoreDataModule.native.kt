package com.techullurgy.howzapp.core.data.impl.di

import org.koin.core.annotation.Module

@Module
internal actual class CoreDataPlatformModule {
    @Single
    actual fun provideAppLifecycleObserver(scope: Scope): AppLifecycleObserver {
        TODO()
    }

    @Single
    actual fun provideConnectionErrorHandler(scope: Scope): ConnectionErrorHandler {
        TODO()
    }

    @Single
    actual fun provideConnectivityObserver(scope: Scope): AppConnectivityObserver {
        TODO()
    }
}