package com.techullurgy.howzapp.core.data.networking

import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.coroutineContext

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> AppResult<T, DataError.Remote>
): AppResult<T, DataError.Remote> {
    return try {
        val response = execute()
        handleResponse(response)
    } catch(_: UnknownHostException) {
        AppResult.Failure(DataError.Remote.NO_INTERNET)
    } catch(_: UnresolvedAddressException) {
        AppResult.Failure(DataError.Remote.NO_INTERNET)
    } catch(_: ConnectException) {
        AppResult.Failure(DataError.Remote.NO_INTERNET)
    } catch(_: SocketTimeoutException) {
        AppResult.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch(_: HttpRequestTimeoutException) {
        AppResult.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch(_: SerializationException) {
        AppResult.Failure(DataError.Remote.SERIALIZATION)
    } catch (_: Exception) {
        coroutineContext.ensureActive()
        AppResult.Failure(DataError.Remote.UNKNOWN)
    }
}