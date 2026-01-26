package com.techullurgy.howzapp.core.network

import com.techullurgy.howzapp.common.utils.models.AppResult
import com.techullurgy.howzapp.common.utils.models.DataError
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse

@PublishedApi
internal expect suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> AppResult<T, DataError.Remote>
): AppResult<T, DataError.Remote>

@PublishedApi
internal suspend inline fun <reified Request, reified Response : Any> HttpClient.post(
    route: String,
    body: Request,
    queryParams: Map<String, Any> = mapOf(),
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): AppResult<Response, DataError.Remote> {
    return safeCall {
        post {
            url(constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            setBody(body)
            builder()
        }
    }
}

@PublishedApi
internal suspend inline fun <reified Response : Any> HttpClient.get(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): AppResult<Response, DataError.Remote> {
    return safeCall {
        get {
            url(constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            builder()
        }
    }
}

@PublishedApi
internal suspend inline fun <reified Response : Any> HttpClient.delete(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): AppResult<Response, DataError.Remote> {
    return safeCall {
        delete {
            url(constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            builder()
        }
    }
}

@PublishedApi
internal suspend inline fun <reified Request, reified Response : Any> HttpClient.put(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    body: Request,
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): AppResult<Response, DataError.Remote> {
    return safeCall {
        put {
            url(constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            setBody(body)
            builder()
        }
    }
}

internal suspend fun HttpClient.webSocketSession(
    route: String,
    builder: HttpRequestBuilder.() -> Unit = {},
): DefaultClientWebSocketSession {
    return webSocketSession {
        url(constructRoute(route, true))
        builder()
    }
}

internal fun HttpClient.clearAuthToken() {
    authProvider<BearerAuthProvider>()?.clearToken()
}

@PublishedApi
internal suspend inline fun <reified T> safeCall(
    noinline execute: suspend () -> HttpResponse
): AppResult<T, DataError.Remote> {
    return platformSafeCall(
        execute = execute
    ) { response ->
        responseToResult(response)
    }
}

@PublishedApi
internal suspend inline fun <reified T> responseToResult(response: HttpResponse): AppResult<T, DataError.Remote> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                AppResult.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                AppResult.Failure(DataError.Remote.SERIALIZATION)
            }
        }
        400 -> AppResult.Failure(DataError.Remote.BAD_REQUEST)
        401 -> AppResult.Failure(DataError.Remote.UNAUTHORIZED)
        403 -> AppResult.Failure(DataError.Remote.FORBIDDEN)
        404 -> AppResult.Failure(DataError.Remote.NOT_FOUND)
        408 -> AppResult.Failure(DataError.Remote.REQUEST_TIMEOUT)
        409 -> AppResult.Failure(DataError.Remote.CONFLICT)
        413 -> AppResult.Failure(DataError.Remote.PAYLOAD_TOO_LARGE)
        429 -> AppResult.Failure(DataError.Remote.TOO_MANY_REQUESTS)
        500 -> AppResult.Failure(DataError.Remote.SERVER_ERROR)
        503 -> AppResult.Failure(DataError.Remote.SERVICE_UNAVAILABLE)
        else -> AppResult.Failure(DataError.Remote.UNKNOWN)
    }
}

@PublishedApi
internal fun constructRoute(route: String, websocketConnection: Boolean = false): String {
    val scheme = if (websocketConnection) UrlConstants.SCHEME_WS else UrlConstants.SCHEME_HTTP
    val baseUrl = "$scheme://${UrlConstants.BASE_URL_HOST}"

    return when {
        route.startsWith(baseUrl) -> route
        route.startsWith("/") -> "$baseUrl$$route"
        else -> "$baseUrl/$route"
    }
}