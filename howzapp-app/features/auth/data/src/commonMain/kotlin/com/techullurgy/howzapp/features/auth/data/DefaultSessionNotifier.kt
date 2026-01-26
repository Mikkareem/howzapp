package com.techullurgy.howzapp.features.auth.data

import com.techullurgy.howzapp.core.datastore.AppDataStore
import com.techullurgy.howzapp.core.datastore.longAppPreferencesKey
import com.techullurgy.howzapp.core.datastore.stringAppPreferencesKey
import com.techullurgy.howzapp.core.session.SessionInfo
import com.techullurgy.howzapp.core.session.SessionNotifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
internal class DefaultSessionNotifier(
    private val dataStore: AppDataStore
) : SessionNotifier {
    private val lastSyncTimestampKey = longAppPreferencesKey("LAST_SYNC_TIMESTAMP_KEY")
    private val sessionInfoKey = stringAppPreferencesKey("SESSION_INFO_KEY")

    private val json = Json { ignoreUnknownKeys = true }

    override fun observeLastSyncTimestamp(): Flow<Long> {
        return dataStore.data.map { pref ->
            pref[lastSyncTimestampKey] ?: 0L
        }
    }

    override fun observeSessionInfo(): Flow<SessionInfo?> {
        return dataStore.data.map { pref ->
            val serialized = pref[sessionInfoKey]
            serialized?.let {
                json.decodeFromString<SessionInfo>(it)
            }
        }
    }

    override suspend fun setLastSyncTimestamp(timestamp: Long) {
        dataStore.edit { prefs ->
            prefs[lastSyncTimestampKey] = timestamp
        }
    }

    override suspend fun setSessionInfo(session: SessionInfo?) {
        if (session == null) {
            dataStore.edit {
                it.remove(sessionInfoKey)
            }
            return
        }

        val serialized = json.encodeToString(session)
        dataStore.edit {
            it[sessionInfoKey] = serialized
        }
    }
}