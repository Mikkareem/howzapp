package com.techullurgy.howzapp.feature.chat.database.di

import com.techullurgy.howzapp.feature.chat.database.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<DatabaseFactory> { DatabaseFactory(get()) }
}