package com.techullurgy.howzapp.core.data.impl

import com.techullurgy.howzapp.common.models.ConnectionState

internal actual class ConnectionErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        TODO("Not yet implemented")
    }

    actual fun transformException(exception: Throwable): Throwable {
        TODO("Not yet implemented")
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        TODO("Not yet implemented")
    }
}