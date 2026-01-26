package com.techullurgy.howzapp.database.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.techullurgy.howzapp.core.database.AppDatabase
import com.techullurgy.howzapp.database.DatabaseFactory
import com.techullurgy.howzapp.database.DefaultAppDatabase
import com.techullurgy.howzapp.database.HowzappDatabase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal expect class DatabasePlatformModule() {
    @Single
    fun provideDatabaseFactory(scope: Scope): DatabaseFactory
}

@Module
internal expect class DatabasePlatformTestModule() {
    @Single
    fun provideDatabaseFactory(scope: Scope): DatabaseFactory
}

@Module(includes = [DatabasePlatformModule::class, DatabaseDaoModule::class])
class DatabaseModule {
    @Single
    fun provideHowzappDatabase(factory: DatabaseFactory): HowzappDatabase {
        return factory.create().setDriver(BundledSQLiteDriver()).build()
    }

    @Single
    fun provideAppDatabase(db: HowzappDatabase): AppDatabase {
        return DefaultAppDatabase(db)
    }
}

@Module(includes = [DatabasePlatformTestModule::class, DatabaseDaoModule::class])
class ChatDatabaseTestModule {
    @Single
    fun provideHowzappDatabase(factory: DatabaseFactory): HowzappDatabase {
        return factory.create().build()
    }
}