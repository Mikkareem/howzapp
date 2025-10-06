package com.techullurgy.howzapp.core.data.di

import com.techullurgy.howzapp.core.data.auth.DatastoreSessionStorage
import com.techullurgy.howzapp.core.data.networking.ByteArrayUploadClient
import com.techullurgy.howzapp.core.data.networking.HttpClientFactory
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.core.domain.networking.UploadClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val platformModule: Module

private val coroutineModule = module {
    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }
    factory<CoroutineDispatcher>(named(IO_DISPATCHER)) {
        get<DispatcherProvider>().ioDispatcher
    }
    factory<CoroutineDispatcher>(named(DEFAULT_DISPATCHER)) {
        get<DispatcherProvider>().defaultDispatcher
    }
    factory<CoroutineDispatcher>(named(MAIN_DISPATCHER)) {
        get<DispatcherProvider>().mainDispatcher
    }

    single<CoroutineScope> {
        CoroutineScope(
            SupervisorJob() + get<CoroutineDispatcher>(named(IO_DISPATCHER))
        )
    }
}

val coreDataModule = module {
    includes(platformModule, coroutineModule)

    single<HttpClient> {
        HttpClientFactory(get()).create()
    }

    single<UploadClient<ByteArray>> {
        ByteArrayUploadClient(get())
    }

    single<SessionStorage> { DatastoreSessionStorage(get()) }
}