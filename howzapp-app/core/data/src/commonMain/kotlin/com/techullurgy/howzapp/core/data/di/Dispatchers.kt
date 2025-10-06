package com.techullurgy.howzapp.core.data.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

const val IO_DISPATCHER = "io_dispatcher"
const val MAIN_DISPATCHER = "main_dispatcher"
const val DEFAULT_DISPATCHER = "default_dispatcher"

interface DispatcherProvider {
    val ioDispatcher: CoroutineDispatcher
    val defaultDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
}

class DefaultDispatcherProvider: DispatcherProvider {
    override val defaultDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default
    override val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
    override val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main
}