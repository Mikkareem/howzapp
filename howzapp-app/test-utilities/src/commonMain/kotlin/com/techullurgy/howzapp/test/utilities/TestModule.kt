package com.techullurgy.howzapp.test.utilities

import com.techullurgy.howzapp.core.data.di.DispatcherProvider
import org.koin.dsl.module

val testModule = module {
    single<DispatcherProvider> { TestDispatchers() }
}