package com.techullurgy.howzapp.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
internal actual class PlatformModule {
    @Single
    actual fun provideDataStore(scope: Scope): DataStore<Preferences> {
        TODO("Not yet implemented")
    }
}