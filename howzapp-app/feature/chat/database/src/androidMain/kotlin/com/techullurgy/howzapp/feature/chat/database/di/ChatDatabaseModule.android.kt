package com.techullurgy.howzapp.feature.chat.database.di

import android.content.Context
import com.techullurgy.howzapp.feature.chat.database.DatabaseFactory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal actual class PlatformModule {
    @Single
    actual fun provideDatabaseFactory(scope: Scope): DatabaseFactory {
        return DatabaseFactory(scope.get<Context>())
    }
}