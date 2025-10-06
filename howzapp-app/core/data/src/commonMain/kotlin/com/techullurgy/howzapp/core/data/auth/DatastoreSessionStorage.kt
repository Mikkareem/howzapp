package com.techullurgy.howzapp.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.techullurgy.howzapp.core.data.dto.AuthInfoSerializable
import com.techullurgy.howzapp.core.data.mappers.toDomain
import com.techullurgy.howzapp.core.data.mappers.toSerializable
import com.techullurgy.howzapp.core.domain.auth.AuthInfo
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DatastoreSessionStorage(
    private val dataStore: DataStore<Preferences>
): SessionStorage {
    private val authInfoKey = stringPreferencesKey("AUTH_INFO_KEY")

    private val json = Json { ignoreUnknownKeys = true }

    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return dataStore.data.map { preferences ->
            val serializedJson = preferences[authInfoKey]
            serializedJson?.let {
                json.decodeFromString<AuthInfoSerializable>(it).toDomain()
            }
        }
    }

    override suspend fun set(auth: AuthInfo?) {
        if(auth == null) {
            dataStore.edit {
                it.remove(authInfoKey)
            }
            return
        }

        val serialized = json.encodeToString(auth.toSerializable())
        dataStore.edit { prefs ->
            prefs[authInfoKey] = serialized
        }
    }
}