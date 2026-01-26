package com.techullurgy.howzapp.database.di

import com.techullurgy.howzapp.database.DatabaseFactory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal actual class DatabasePlatformModule {
    @Single
    actual fun provideDatabaseFactory(scope: Scope): DatabaseFactory {
        TODO("Not yet implemented")
    }
}

@Module
internal actual class DatabasePlatformTestModule {
    @Single
    actual fun provideDatabaseFactory(scope: Scope): DatabaseFactory {
        TODO("Not yet implemented")
    }
}