package com.techullurgy.howzapp.feature.chat.domain.services

import kotlinx.coroutines.flow.Flow

interface ChatSynchronizer {
    val syncsFlow: Flow<Unit>

    fun triggerSync()
}