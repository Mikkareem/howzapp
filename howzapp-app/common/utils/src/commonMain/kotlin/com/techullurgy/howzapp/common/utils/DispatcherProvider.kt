package com.techullurgy.howzapp.common.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.Single

interface DispatcherProvider {
    val ioDispatcher: CoroutineDispatcher
    val defaultDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
}

@Single
internal class DefaultDispatcherProvider : DispatcherProvider {
    override val defaultDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default
    override val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
    override val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main
}