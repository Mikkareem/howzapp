package com.techullurgy.howzapp.core.data.impl

import com.techullurgy.howzapp.core.data.api.AppConnectivityObserver
import kotlinx.coroutines.flow.Flow

internal actual class PlatformConnectivityObserver : AppConnectivityObserver {
    actual override val isConnected: Flow<Boolean>
        get() = TODO("Not yet implemented")
}