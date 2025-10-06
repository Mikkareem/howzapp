package com.techullurgy.howzapp.feature.chat.database.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.techullurgy.howzapp.feature.chat.database.DatabaseFactory
import com.techullurgy.howzapp.feature.chat.database.HowzappDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val chatDatabaseModule = module {
    includes(platformModule)
    single<HowzappDatabase> {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}