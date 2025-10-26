package com.techullurgy.howzapp.feature.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow

actual class PlatformAppLifecycleObserver : AppLifecycleObserver {
    override val isInForeground: Flow<Boolean>
        get() = TODO("Not yet implemented")
}