package com.techullurgy.howzapp.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal actual class CoreDataStorePlatformModule {
    @Single
    internal actual fun provideDataStore(scope: Scope): DataStore<Preferences> {
        TODO("Not yet implemented")
    }
}