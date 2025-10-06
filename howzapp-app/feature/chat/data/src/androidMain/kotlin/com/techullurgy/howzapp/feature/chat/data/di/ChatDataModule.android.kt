package com.techullurgy.howzapp.feature.chat.data.di

import com.techullurgy.howzapp.feature.chat.data.lifecycle.AppLifecycleObserver
import com.techullurgy.howzapp.feature.chat.data.networking.ConnectionErrorHandler
import com.techullurgy.howzapp.feature.chat.data.networking.ConnectivityObserver
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single<ConnectivityObserver> { ConnectivityObserver(get()) }
    single<AppLifecycleObserver> { AppLifecycleObserver() }
    single { ConnectionErrorHandler() }
}