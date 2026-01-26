package com.techullurgy.howzapp.database.di

import android.content.Context
import com.techullurgy.howzapp.database.DatabaseFactory
import com.techullurgy.howzapp.database.TestDatabaseFactory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal actual class DatabasePlatformModule {
    @Single
    actual fun provideDatabaseFactory(scope: Scope): DatabaseFactory {
        return DatabaseFactory(scope.get<Context>())
    }
}

@Module
internal actual class DatabasePlatformTestModule {
    @Single
    actual fun provideDatabaseFactory(scope: Scope): DatabaseFactory {
        return TestDatabaseFactory(scope.get<Context>())
    }
}