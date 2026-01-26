package com.techullurgy.howzapp.core.data.impl.di

import com.techullurgy.howzapp.core.data.api.AppConnectivityObserver
import com.techullurgy.howzapp.core.data.api.AppHttpConnector
import com.techullurgy.howzapp.core.data.api.AppLifecycleObserver
import com.techullurgy.howzapp.core.data.api.AppUploadClient
import com.techullurgy.howzapp.core.data.impl.ByteArrayUploadClient
import com.techullurgy.howzapp.core.data.impl.ConnectionErrorHandler
import com.techullurgy.howzapp.core.network.AppHttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal expect class CoreDataPlatformModule {
    @Single
    fun provideAppLifecycleObserver(scope: Scope): AppLifecycleObserver

    @Single
    fun provideConnectionErrorHandler(scope: Scope): ConnectionErrorHandler

    @Single
    fun provideConnectivityObserver(scope: Scope): AppConnectivityObserver
}

@Module(includes = [CoreDataPlatformModule::class])
@ComponentScan("com.techullurgy.howzapp.core.data.impl")
class CoreDataModule {
    @Single
    internal fun provideAppUploadClient(httpConnector: AppHttpConnector): AppUploadClient<ByteArray> {
        return ByteArrayUploadClient(httpConnector)
    }

    @Single
    internal fun provideAppHttpConnector(appClient: AppHttpClient): AppHttpConnector {
        return object : AppHttpConnector(appClient) {}
    }
}