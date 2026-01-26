package com.techullurgy.howzapp.core.data.impl

import com.techullurgy.howzapp.common.models.ConnectionState

internal expect class ConnectionErrorHandler {
    fun getConnectionStateForError(cause: Throwable): ConnectionState
    fun transformException(exception: Throwable): Throwable
    fun isRetriableError(cause: Throwable): Boolean
}