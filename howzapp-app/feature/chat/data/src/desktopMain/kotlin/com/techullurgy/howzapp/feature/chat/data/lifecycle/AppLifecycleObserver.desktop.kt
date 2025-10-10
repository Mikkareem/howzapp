package com.techullurgy.howzapp.feature.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
actual class AppLifecycleObserver {
    actual val isInForeground: Flow<Boolean>
        get() = TODO("Not yet implemented")
}