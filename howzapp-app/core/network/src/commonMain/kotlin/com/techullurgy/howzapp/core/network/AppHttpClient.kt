package com.techullurgy.howzapp.core.network

import com.techullurgy.howzapp.common.utils.models.AppResult
import com.techullurgy.howzapp.common.utils.models.DataError
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class AppHttpClient(
    @PublishedApi internal val client: NetworkClient
) {
    fun upload(
        url: String,
        source: ByteArray,
        headers: Map<String, String> = mapOf()
    ): Flow<NetworkClient.UploadStatus> {
        return client.upload(
            url = url,
            source = source,
            headers = headers
        )
    }

    suspend inline fun <reified Request : Any, reified Response : Any> post(
        route: String,
        body: Request,
        headers: Map<String, String> = mapOf(),
        queryParams: Map<String, String> = mapOf()
    ): AppResult<Response, DataError.Remote> {
        return client.post<Request, Response>(
            route = route,
            body = body,
            headers = headers,
            queryParams = queryParams
        )
    }

    suspend inline fun <reified Response : Any> get(
        route: String,
        headers: Map<String, String> = mapOf(),
        queryParams: Map<String, String> = mapOf()
    ): AppResult<Response, DataError.Remote> {
        return client.get<Response>(
            route = route,
            headers = headers,
            queryParams = queryParams
        )
    }

    suspend inline fun <reified Response : Any> delete(
        route: String,
        headers: Map<String, String> = mapOf(),
        queryParams: Map<String, String> = mapOf()
    ): AppResult<Response, DataError.Remote> {
        return client.delete(
            route = route,
            headers = headers,
            queryParams = queryParams
        )
    }

    suspend inline fun <reified Request : Any, reified Response : Any> put(
        route: String,
        body: Request,
        headers: Map<String, String> = mapOf(),
        queryParams: Map<String, String> = mapOf()
    ): AppResult<Response, DataError.Remote> {
        return client.put<Request, Response>(
            route = route,
            body = body,
            headers = headers,
            queryParams = queryParams
        )
    }

    fun clearAuthToken() {
        client.clearAuthToken()
    }
}