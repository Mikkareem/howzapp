package com.techullurgy.howzapp.core.data.impl

import com.techullurgy.howzapp.core.data.api.AppLifecycleObserver
import kotlinx.coroutines.flow.Flow

internal actual class PlatformAppLifecycleObserver : AppLifecycleObserver {
    actual override val isInForeground: Flow<Boolean>
        get() = TODO("Not yet implemented")
}