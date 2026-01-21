package com.techullurgy.howzapp.feature.auth.api.data

import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    fun observeAuthInfo(): Flow<AuthInfo?>
    suspend fun setAuthInfo(auth: AuthInfo?)

    fun observeLastSyncTimestamp(): Flow<Long>
    suspend fun setLastSyncTimestamp(timestamp: Long)
}