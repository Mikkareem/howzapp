package com.techullurgy.howzapp.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.techullurgy.howzapp.core.data.networking.ByteArrayUploadClient
import com.techullurgy.howzapp.core.data.networking.HOST_URL
import com.techullurgy.howzapp.core.data.networking.HttpClientFactory
import com.techullurgy.howzapp.core.domain.networking.UploadClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Qualifier
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope


@Qualifier
annotation class IoDispatcher

@Qualifier
annotation class DefaultDispatcher

@Qualifier
annotation class MainDispatcher

@Qualifier
annotation class HostAndPort

@Module
internal class CoroutinesModule {

    @Factory
    @IoDispatcher
    fun provideIoDispatcher(provider: DispatcherProvider): CoroutineDispatcher =
        provider.ioDispatcher

    @Factory
    @DefaultDispatcher
    fun provideDefaultDispatcher(provider: DispatcherProvider): CoroutineDispatcher =
        provider.defaultDispatcher

    @Factory
    @MainDispatcher
    fun provideMainDispatcher(provider: DispatcherProvider): CoroutineDispatcher =
        provider.mainDispatcher

    @Single
    fun provideApplicationScope(
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatcher)
    }
}

@Module
internal class BaseUrlModule {
    @Single
    @HostAndPort
    fun provideHostAndPort(): String {
        return "$HOST_URL:8080"
    }
}

@Module
internal expect class PlatformModule {
    @Single
    fun provideDataStore(scope: Scope): DataStore<Preferences>
}


@Module(includes = [CoroutinesModule::class, BaseUrlModule::class, PlatformModule::class])
class CoreDataModule {

    @Single
    fun provideHttpClient(scope: Scope): HttpClient {
        return scope.get<HttpClientFactory>().create()
    }

    @Single
    fun provideUploadClient(client: HttpClient): UploadClient<ByteArray> {
        return ByteArrayUploadClient(client)
    }
}