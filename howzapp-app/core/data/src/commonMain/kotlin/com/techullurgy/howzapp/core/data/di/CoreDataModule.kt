package com.techullurgy.howzapp.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.techullurgy.howzapp.core.data.networking.ByteArrayUploadClient
import com.techullurgy.howzapp.core.data.networking.HttpClientFactory
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.core.domain.networking.UploadClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.Configuration
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

@Module
@Configuration
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
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatcher)
    }
}

@Module
@Configuration
internal expect class PlatformModule {
    @Single
    fun provideDataStore(scope: Scope): DataStore<Preferences>
}


@Module(includes = [CoroutinesModule::class, PlatformModule::class])
@Configuration
class CoreDataModule {

    @Single
    fun provideHttpClient(sessionStorage: SessionStorage): HttpClient {
        return HttpClientFactory(sessionStorage).create()
    }

    @Single
    fun provideUploadClient(client: HttpClient): UploadClient<ByteArray> {
        return ByteArrayUploadClient(client)
    }
}