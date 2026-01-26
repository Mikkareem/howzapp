package com.techullurgy.howzapp.core.data.impl

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.techullurgy.howzapp.common.utils.MainDispatcher
import com.techullurgy.howzapp.core.data.api.AppLifecycleObserver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

internal actual class PlatformAppLifecycleObserver(
    @MainDispatcher mainDispatcher: CoroutineDispatcher
) : AppLifecycleObserver {
    actual override val isInForeground: Flow<Boolean> = callbackFlow {
        val lifecycle = ProcessLifecycleOwner.get().lifecycle

        lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED).also { send(it) }

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> trySend(true)
                Lifecycle.Event.ON_STOP -> trySend(false)
                else -> Unit
            }
        }

        lifecycle.addObserver(observer)

        awaitClose { lifecycle.removeObserver(observer) }
    }.flowOn(mainDispatcher)
}