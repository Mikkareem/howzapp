package com.techullurgy.howzapp.core.data.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Single

interface DispatcherProvider {
    val ioDispatcher: CoroutineDispatcher
    val defaultDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
}

@Single
class DefaultDispatcherProvider: DispatcherProvider {
    override val defaultDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default
    override val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
    override val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main
}