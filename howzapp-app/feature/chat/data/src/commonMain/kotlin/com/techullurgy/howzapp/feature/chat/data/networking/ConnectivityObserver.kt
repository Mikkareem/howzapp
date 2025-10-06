package com.techullurgy.howzapp.feature.chat.data.networking

import kotlinx.coroutines.flow.Flow

expect class ConnectivityObserver {
    val isConnected: Flow<Boolean>
}