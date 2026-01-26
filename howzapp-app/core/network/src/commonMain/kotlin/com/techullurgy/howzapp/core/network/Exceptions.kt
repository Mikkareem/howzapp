package com.techullurgy.howzapp.core.network

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.websocket.WebSocketException
import kotlinx.io.EOFException

class SocketTimeoutException(
    parent: SocketTimeoutException
) : Throwable(parent.message, parent)

class EOFException(
    parent: EOFException
) : Throwable(parent.message, parent)

class WebSocketException(
    parent: WebSocketException
) : Throwable(parent.message, parent)

class ClientRequestException(
    parent: ClientRequestException
) : Throwable(parent.message, parent)

internal fun Throwable.transformKtorThrowable(): Throwable {
    return when (this) {
        is SocketTimeoutException -> return SocketTimeoutException(this)
        is EOFException -> return EOFException(this)
        is WebSocketException -> return WebSocketException(this)
        is ClientRequestException -> return ClientRequestException(this)
        else -> this
    }
}