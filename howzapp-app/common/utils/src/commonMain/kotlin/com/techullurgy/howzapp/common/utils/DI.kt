package com.techullurgy.howzapp.common.utils

import org.koin.mp.KoinPlatform

inline fun <reified T : Any> inject(): T {
    return KoinPlatform.getKoin().get<T>()
}