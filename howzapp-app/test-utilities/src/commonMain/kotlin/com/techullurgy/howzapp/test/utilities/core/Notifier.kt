package com.techullurgy.howzapp.test.utilities.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Notifier<T> {
    private val internalFlow = MutableSharedFlow<T>()

    val flow: Flow<T> = internalFlow.asSharedFlow()

    suspend fun send(value: T) {
        internalFlow.emit(value)
    }
}