package com.techullurgy.howzapp.feature.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow

interface AppLifecycleObserver {
    val isInForeground: Flow<Boolean>
}

expect class PlatformAppLifecycleObserver : AppLifecycleObserver