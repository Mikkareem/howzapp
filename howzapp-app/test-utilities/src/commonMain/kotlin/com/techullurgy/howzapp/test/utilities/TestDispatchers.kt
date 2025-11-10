package com.techullurgy.howzapp.test.utilities

import com.techullurgy.howzapp.core.data.di.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

internal class TestDispatchers: DispatcherProvider {
    override val defaultDispatcher: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val ioDispatcher: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val mainDispatcher: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
}