package com.techullurgy.howzapp.core.network.di

import com.techullurgy.howzapp.core.network.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.techullurgy.howzapp.core.network")
class CoreNetworkModule {

    @Single
    internal fun provideHttpClient(factory: HttpClientFactory): HttpClient = factory.create()

    @Single
    internal fun provideHttpClientEngine(): HttpClientEngine = CIO.create()
}