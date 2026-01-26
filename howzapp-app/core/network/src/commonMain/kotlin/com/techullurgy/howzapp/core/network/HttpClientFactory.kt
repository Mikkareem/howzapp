package com.techullurgy.howzapp.core.network

import com.techullurgy.howzapp.common.utils.models.onFailure
import com.techullurgy.howzapp.common.utils.models.onSuccess
import com.techullurgy.howzapp.core.session.SessionInfo
import com.techullurgy.howzapp.core.session.SessionNotifier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
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
import org.koin.core.annotation.Single

@Single
internal class HttpClientFactory(
    private val sessionNotifier: SessionNotifier,
    private val engine: HttpClientEngine
) {
    fun create(): HttpClient {
        return HttpClient(engine) {
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
                        sessionNotifier
                            .observeSessionInfo()
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

                        val sessionInfo = sessionNotifier.observeSessionInfo().firstOrNull()
                        if (sessionInfo?.refreshToken.isNullOrBlank()) {
                            sessionNotifier.setSessionInfo(null)
                            return@refreshTokens null
                        }

                        var bearerTokens: BearerTokens? = null
                        client.post<Map<String, String>, SessionInfo>(
                            route = "/api/auth/refresh",
                            body = mapOf(
                                "refreshToken" to sessionInfo.refreshToken
                            ),
                            builder = {
                                markAsRefreshTokenRequest()
                            }
                        ).onSuccess { newSessionInfo ->
                            sessionNotifier.setSessionInfo(newSessionInfo)
                            bearerTokens = BearerTokens(
                                refreshToken = newSessionInfo.refreshToken,
                                accessToken = newSessionInfo.accessToken
                            )
                        }.onFailure {
                            sessionNotifier.setSessionInfo(null)
                        }

                        bearerTokens
                    }
                }
            }
        }
    }
}