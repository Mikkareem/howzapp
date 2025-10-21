package com.techullurgy.howzapp.feature.chat.data.networking

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
}

expect class PlatformConnectivityObserver: ConnectivityObserver