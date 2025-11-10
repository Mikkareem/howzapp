package com.techullurgy.howzapp.feature.chat.data.networking

import com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState
import org.koin.core.annotation.Single

@Single
expect class ConnectionErrorHandler {
    fun getConnectionStateForError(cause: Throwable): ConnectionState
    fun transformException(exception: Throwable): Throwable
    fun isRetriableError(cause: Throwable): Boolean
}