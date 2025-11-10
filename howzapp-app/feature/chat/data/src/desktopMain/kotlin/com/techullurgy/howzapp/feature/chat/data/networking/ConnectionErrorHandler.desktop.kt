package com.techullurgy.howzapp.feature.chat.data.networking

import org.koin.core.annotation.Single

@Single
actual class ConnectionErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): com.techullurgy.howzapp.feature.chat.domain.models.ConnectionState {
        TODO("Not yet implemented")
    }

    actual fun transformException(exception: Throwable): Throwable {
        TODO("Not yet implemented")
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        TODO("Not yet implemented")
    }
}