package com.techullurgy.howzapp.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal expect class CoreDataStorePlatformModule() {
    @Single
    internal fun provideDataStore(scope: Scope): DataStore<Preferences>
}

@Module(includes = [CoreDataStorePlatformModule::class])
@ComponentScan("com.techullurgy.howzapp.core.datastore")
class CoreDataStoreModule