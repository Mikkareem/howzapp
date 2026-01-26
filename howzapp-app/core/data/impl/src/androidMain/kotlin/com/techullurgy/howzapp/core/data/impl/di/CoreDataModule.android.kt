package com.techullurgy.howzapp.core.data.impl.di

import android.content.Context
import com.techullurgy.howzapp.common.utils.MainDispatcher
import com.techullurgy.howzapp.core.data.api.AppConnectivityObserver
import com.techullurgy.howzapp.core.data.api.AppLifecycleObserver
import com.techullurgy.howzapp.core.data.impl.ConnectionErrorHandler
import com.techullurgy.howzapp.core.data.impl.PlatformAppLifecycleObserver
import com.techullurgy.howzapp.core.data.impl.PlatformConnectivityObserver
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.qualifier.qualifier
import org.koin.core.scope.Scope

@Module
internal actual class CoreDataPlatformModule {
    @Single
    actual fun provideAppLifecycleObserver(scope: Scope): AppLifecycleObserver {
        return PlatformAppLifecycleObserver(scope.get(qualifier = qualifier<MainDispatcher>()))
    }

    @Single
    actual fun provideConnectionErrorHandler(scope: Scope): ConnectionErrorHandler {
        return ConnectionErrorHandler()
    }

    @Single
    actual fun provideConnectivityObserver(scope: Scope): AppConnectivityObserver {
        return PlatformConnectivityObserver(scope.get<Context>())
    }
}