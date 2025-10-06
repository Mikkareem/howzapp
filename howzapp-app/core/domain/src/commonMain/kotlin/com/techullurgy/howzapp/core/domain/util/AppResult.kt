package com.techullurgy.howzapp.core.domain.util

interface Error

sealed interface AppResult<out D, out E: Error> {
    data class Success<out D>(val data: D): AppResult<D, Nothing>
    data class Failure<out E: Error>(val error: E): AppResult<Nothing, E>
}

inline fun <T, E: Error, R> AppResult<T, E>.map(transform: (T) -> R): AppResult<R, E> {
    return when(this) {
        is AppResult.Failure -> AppResult.Failure(error)
        is AppResult.Success -> AppResult.Success(transform(data))
    }
}

inline fun <T, E: Error> AppResult<T, E>.onSuccess(action: (T) -> Unit): AppResult<T, E> {
    return when(this) {
        is AppResult.Failure -> this
        is AppResult.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E: Error> AppResult<T, E>.onFailure(action: (E) -> Unit): AppResult<T, E> {
    return when(this) {
        is AppResult.Success -> this
        is AppResult.Failure -> {
            action(error)
            this
        }
    }
}

fun <T, E: Error> AppResult<T, E>.asEmptyResult(): EmptyResult<E> {
    return map {  }
}

typealias EmptyResult<E> = AppResult<Unit, E>