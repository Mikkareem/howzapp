package com.techullurgy.howzapp.feature.chat.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
internal data class SyncRequest(
    val lastSyncTimestamp: Long,
)