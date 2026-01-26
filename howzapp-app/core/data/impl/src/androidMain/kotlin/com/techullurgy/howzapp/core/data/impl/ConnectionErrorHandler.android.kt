package com.techullurgy.howzapp.core.data.impl

import com.techullurgy.howzapp.core.network.ClientRequestException
import com.techullurgy.howzapp.core.network.EOFException
import com.techullurgy.howzapp.core.network.SocketTimeoutException
import com.techullurgy.howzapp.core.network.WebSocketException
import com.techullurgy.howzapp.common.models.ConnectionState
import org.koin.core.annotation.Single
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

@Single
internal actual class ConnectionErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        return when (cause) {
            is ClientRequestException,
            is WebSocketException,
            is SocketException,
            is SocketTimeoutException,
            is UnknownHostException,
            is SSLException,
            is EOFException -> ConnectionState.ERROR_NETWORK
            else -> ConnectionState.ERROR_UNKNOWN
        }
    }

    actual fun transformException(exception: Throwable): Throwable = exception

    actual fun isRetriableError(cause: Throwable): Boolean {
        return when (cause) {
            is SocketTimeoutException,
            is WebSocketException,
            is SocketException,
            is UnknownHostException,
            is EOFException -> true
            else -> false
        }
    }
}
