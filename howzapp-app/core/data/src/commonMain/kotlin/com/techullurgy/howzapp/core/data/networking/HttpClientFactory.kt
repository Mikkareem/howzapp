package com.techullurgy.howzapp.core.data.networking

import com.techullurgy.howzapp.core.domain.auth.AuthInfo
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.core.domain.util.onFailure
import com.techullurgy.howzapp.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val sessionStorage: SessionStorage
) {
    fun create(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 20_000
                requestTimeoutMillis = 20_000
                connectTimeoutMillis = 20_000
            }

            install(WebSockets) {
                pingIntervalMillis = 20_000
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        sessionStorage
                            .observeAuthInfo()
                            .firstOrNull()
                            ?.let {
                                BearerTokens(
                                    accessToken = it.accessToken,
                                    refreshToken = it.refreshToken
                                )
                            }
                    }

                    refreshTokens {
                        if(response.request.url.encodedPath.contains("auth/")) {
                            return@refreshTokens null
                        }

                        val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
                        if(authInfo?.refreshToken.isNullOrBlank()) {
                            sessionStorage.setAuthInfo(null)
                            return@refreshTokens null
                        }

                        var bearerTokens: BearerTokens? = null
                        client.post<Map<String, String>, AuthInfo>(
                            route = "/api/auth/refresh",
                            body = mapOf(
                                "refreshToken" to authInfo.refreshToken
                            ),
                            builder = {
                                markAsRefreshTokenRequest()
                            }
                        ).onSuccess { newAuthInfo ->
                            sessionStorage.setAuthInfo(newAuthInfo)
                            bearerTokens = BearerTokens(
                                refreshToken = newAuthInfo.refreshToken,
                                accessToken = newAuthInfo.accessToken
                            )
                        }.onFailure {
                            sessionStorage.setAuthInfo(null)
                        }

                        bearerTokens
                    }
                }
            }
        }
    }
}