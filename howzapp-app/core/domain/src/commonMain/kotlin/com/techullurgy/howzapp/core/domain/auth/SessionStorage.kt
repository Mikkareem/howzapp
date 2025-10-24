package com.techullurgy.howzapp.core.domain.auth

import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    fun observeAuthInfo(): Flow<AuthInfo?>
    suspend fun setAuthInfo(auth: AuthInfo?)

    fun observeLastSyncTimestamp(): Flow<Long>
    suspend fun setLastSyncTimestamp(timestamp: Long)
}