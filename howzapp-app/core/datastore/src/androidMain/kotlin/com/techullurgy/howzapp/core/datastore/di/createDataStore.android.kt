package com.techullurgy.howzapp.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.techullurgy.howzapp.core.datastore.di.DATA_STORE_FILE_NAME
import com.techullurgy.howzapp.core.datastore.di.createDataStore

fun createDataStore(context: Context): DataStore<Preferences> {
    return createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}