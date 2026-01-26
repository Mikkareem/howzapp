package com.techullurgy.howzapp.common.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

fun <T> Flow<Iterable<T>>.flatten(): Flow<T> {
    return transform { iterable ->
        iterable.forEach {
            emit(it)
        }
    }
}

fun <T> Flow<T>.dropDuplicates(): Flow<T> {
    val unique = mutableSetOf<T>()

    return transform {
        if(unique.add(it)) {
            emit(it)
        }
    }
}