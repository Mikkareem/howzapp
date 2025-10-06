package com.techullurgy.howzapp.feature.chat.domain.models

import kotlin.time.Instant

sealed interface OnlineStatus {
    data object NoOnlineStatus: OnlineStatus
    data object IsOnline: OnlineStatus
    data class NotInOnline(val lastSeen: Instant): OnlineStatus
}