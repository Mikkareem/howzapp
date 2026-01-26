package com.techullurgy.howzapp.core.data.impl.di

import com.techullurgy.howzapp.core.data.api.AppConnectivityObserver
import com.techullurgy.howzapp.core.data.api.AppLifecycleObserver
import com.techullurgy.howzapp.core.data.impl.ConnectionErrorHandler
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

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