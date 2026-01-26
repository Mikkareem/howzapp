package com.techullurgy.howzapp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class AppDataStore internal constructor(
    private val datastore: DataStore<Preferences>
) {
    val data = datastore.data
        .map {
            it.toAppPreferences()
        }

    suspend fun edit(
        transform: (EditableAppPreferences) -> Unit
    ) {
        datastore.edit {
            with(EditableAppPreferences(it)) {
                transform(this)
            }
        }
    }
}