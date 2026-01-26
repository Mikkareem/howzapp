package com.techullurgy.howzapp.core.data.api

import com.techullurgy.howzapp.core.network.AppHttpClient
import com.techullurgy.howzapp.core.network.NetworkClient
import com.techullurgy.howzapp.common.utils.models.AppResult
import com.techullurgy.howzapp.common.utils.models.DataError
import kotlinx.coroutines.flow.Flow

abstract class AppHttpConnector(
    @PublishedApi internal val httpClient: AppHttpClient
) {
    fun upload(
        url: String,
        source: ByteArray,
        headers: Map<String, String> = mapOf()
    ): Flow<NetworkClient.UploadStatus> {
        return httpClient.upload(
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
        return httpClient.post<Request, Response>(
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
        return httpClient.get<Response>(
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
        return httpClient.delete(
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
        return httpClient.put<Request, Response>(
            route = route,
            body = body,
            headers = headers,
            queryParams = queryParams
        )
    }

    fun clearAuthToken() {
        httpClient.clearAuthToken()
    }
}