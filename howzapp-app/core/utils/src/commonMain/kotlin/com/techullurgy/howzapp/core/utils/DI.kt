package com.techullurgy.howzapp.core.utils

import org.koin.mp.KoinPlatform

inline fun <reified T : Any> inject(): T {
    return KoinPlatform.getKoin().get<T>()
}