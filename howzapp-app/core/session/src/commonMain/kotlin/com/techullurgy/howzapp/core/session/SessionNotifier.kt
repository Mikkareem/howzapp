package com.techullurgy.howzapp.core.session

import kotlinx.coroutines.flow.Flow

interface SessionNotifier {
    fun observeSessionInfo(): Flow<SessionInfo?>
    fun observeLastSyncTimestamp(): Flow<Long>

    suspend fun setSessionInfo(session: SessionInfo?)
    suspend fun setLastSyncTimestamp(timestamp: Long)
}