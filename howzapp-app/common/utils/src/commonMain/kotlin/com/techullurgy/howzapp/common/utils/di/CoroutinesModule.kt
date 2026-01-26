package com.techullurgy.howzapp.common.utils.di

import com.techullurgy.howzapp.common.utils.DefaultDispatcher
import com.techullurgy.howzapp.common.utils.DispatcherProvider
import com.techullurgy.howzapp.common.utils.IoDispatcher
import com.techullurgy.howzapp.common.utils.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

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