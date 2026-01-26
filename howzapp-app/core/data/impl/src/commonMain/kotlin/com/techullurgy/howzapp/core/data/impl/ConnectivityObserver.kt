package com.techullurgy.howzapp.core.data.impl

import com.techullurgy.howzapp.core.data.api.AppConnectivityObserver
import kotlinx.coroutines.flow.Flow

internal expect class PlatformConnectivityObserver : AppConnectivityObserver {
    override val isConnected: Flow<Boolean>
}