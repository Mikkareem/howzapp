package com.techullurgy.howzapp.feature.chat.database.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.techullurgy.howzapp.feature.chat.database.DatabaseFactory
import com.techullurgy.howzapp.feature.chat.database.HowzappDatabase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal expect class PlatformModule {
    @Single
    fun provideDatabaseFactory(scope: Scope): DatabaseFactory
}

@Module
internal expect class PlatformTestModule {
    @Single
    fun provideDatabaseFactory(scope: Scope): DatabaseFactory
}

@Module(includes = [PlatformModule::class])
@ComponentScan
class ChatDatabaseModule {
    @Single
    fun provideHowzappDatabase(factory: DatabaseFactory): HowzappDatabase {
        return factory.create().setDriver(BundledSQLiteDriver()).build()
    }
}

@Module(includes = [PlatformTestModule::class])
class ChatDatabaseTestModule {
    @Single
    fun provideHowzappDatabase(factory: DatabaseFactory): HowzappDatabase {
        return factory.create().build()
    }
}