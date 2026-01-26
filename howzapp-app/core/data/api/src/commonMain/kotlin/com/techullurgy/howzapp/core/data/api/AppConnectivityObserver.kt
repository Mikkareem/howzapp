package com.techullurgy.howzapp.core.data.api

import kotlinx.coroutines.flow.Flow

interface AppConnectivityObserver {
    val isConnected: Flow<Boolean>
}