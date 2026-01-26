package com.techullurgy.howzapp.core.data.api

import kotlinx.coroutines.flow.Flow

interface AppLifecycleObserver {
    val isInForeground: Flow<Boolean>
}