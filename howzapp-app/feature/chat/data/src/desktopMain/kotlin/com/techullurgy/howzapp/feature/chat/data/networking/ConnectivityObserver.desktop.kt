package com.techullurgy.howzapp.feature.chat.data.networking

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

actual class PlatformConnectivityObserver: ConnectivityObserver {
    override val isConnected: Flow<Boolean>
        get() = TODO("Not yet implemented")
}