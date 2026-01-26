package com.techullurgy.howzapp.core.data.impl

import com.techullurgy.howzapp.core.data.api.AppLifecycleObserver
import kotlinx.coroutines.flow.Flow

internal expect class PlatformAppLifecycleObserver : AppLifecycleObserver {
    override val isInForeground: Flow<Boolean>
}