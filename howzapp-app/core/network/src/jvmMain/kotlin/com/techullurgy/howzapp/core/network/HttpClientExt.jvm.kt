package com.techullurgy.howzapp.core.network

import com.techullurgy.howzapp.common.utils.models.AppResult
import com.techullurgy.howzapp.common.utils.models.DataError
import io.ktor.client.statement.HttpResponse

@PublishedApi
internal actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> AppResult<T, DataError.Remote>
): AppResult<T, DataError.Remote> {
    TODO("Not yet implemented")
}