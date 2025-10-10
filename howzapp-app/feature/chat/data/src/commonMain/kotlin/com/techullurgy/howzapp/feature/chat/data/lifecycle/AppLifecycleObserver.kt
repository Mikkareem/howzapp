package com.techullurgy.howzapp.feature.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
expect class AppLifecycleObserver {
    val isInForeground: Flow<Boolean>
}