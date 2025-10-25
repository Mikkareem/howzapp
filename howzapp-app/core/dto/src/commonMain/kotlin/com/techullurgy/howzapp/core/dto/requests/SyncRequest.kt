package com.techullurgy.howzapp.core.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class SyncRequest(
    val lastSyncTimestamp: Long,
)