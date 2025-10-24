package com.techullurgy.howzapp.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.techullurgy.howzapp.core.domain.auth.AuthInfo
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class DatastoreSessionStorage(
    private val dataStore: DataStore<Preferences>
): SessionStorage {
    private val authInfoKey = stringPreferencesKey("AUTH_INFO_KEY")
    private val lastSyncTimestampKey = longPreferencesKey("LAST_SYNC_TIMESTAMP_KEY")

    private val json = Json { ignoreUnknownKeys = true }

    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return dataStore.data.map { preferences ->
            val serializedJson = preferences[authInfoKey]
            serializedJson?.let {
                json.decodeFromString<AuthInfo>(it)
            }
        }
    }

    override suspend fun setAuthInfo(auth: AuthInfo?) {
        if(auth == null) {
            dataStore.edit {
                it.remove(authInfoKey)
            }
            return
        }

        val serialized = json.encodeToString(auth)
        dataStore.edit { prefs ->
            prefs[authInfoKey] = serialized
        }
    }

    override fun observeLastSyncTimestamp(): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[lastSyncTimestampKey] ?: 0L
        }
    }

    override suspend fun setLastSyncTimestamp(timestamp: Long) {
        dataStore.edit { prefs ->
            prefs[lastSyncTimestampKey] = timestamp
        }
    }
}