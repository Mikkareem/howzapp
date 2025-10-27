package com.techullurgy.howzapp.test.utilities.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

open class Notifier<T> {
    private val internalFlow = MutableSharedFlow<T>(replay = 1)

    val flow: Flow<T> = internalFlow.asSharedFlow()

    suspend fun send(value: T) {
        internalFlow.emit(value)
    }
}

open class StateNotifier<T>(initialValue: T) {
    private val internalFlow = MutableStateFlow<T>(initialValue)

    val flow: Flow<T> = internalFlow.asStateFlow()

    suspend fun send(value: T) {
        internalFlow.emit(value)
    }
}