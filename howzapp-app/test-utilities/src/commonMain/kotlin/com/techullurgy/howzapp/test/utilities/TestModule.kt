package com.techullurgy.howzapp.test.utilities

import com.techullurgy.howzapp.core.data.di.DispatcherProvider
import com.techullurgy.howzapp.core.data.di.HostAndPort
import com.techullurgy.howzapp.core.data.networking.HttpClientFactory
import com.techullurgy.howzapp.core.domain.logging.HowzappLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.http.headersOf
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val testModule = module {
    single<DispatcherProvider> { TestDispatchers() }
    single<HowzappLogger> { TestLogger() }

    single<String>(qualifier = qualifier<HostAndPort>()) { "localhost:8080" }
    single<HttpClientEngine> {
        MockEngine.create {
            dispatcher = UnconfinedTestDispatcher()

            addHandler {
                get<AppMockEngine>().handler(it)
            }
        }
    }

    single<AppMockEngine>(createdAtStart = true) { AppMockEngine() }

    single<HttpClient> {
        mockkStatic("io.ktor.client.plugins.websocket.BuildersKt")
        spyk(get<HttpClientFactory>().create()) {
            coEvery {
                this@spyk.webSocketSession(any(), any<HttpRequestBuilder.() -> Unit>())
            } returns get<AppMockEngine>().session
        }
    }
}

class AppMockEngine {
    val mockedIncoming = Channel<Frame>(Channel.CONFLATED)
    val mockedOutgoing = Channel<Frame>(Channel.CONFLATED)

    val session = mockk<DefaultClientWebSocketSession>(
        relaxUnitFun = true,
        relaxed = true
    ) {
        val frameSlot = CapturingSlot<Frame>()
        every { coroutineContext } returns UnconfinedTestDispatcher()
        every { incoming } returns mockedIncoming
        every { outgoing } returns mockedOutgoing

        coEvery { send(capture(frameSlot)) } coAnswers { outgoing.send(frameSlot.captured) }
        coEvery { close() } answers { incoming.cancel() }
    }

    var loginResponseData: ResponseData? = null
    var syncResponseData: ResponseData? = null
    var receiptResponseData: ResponseData? = null

    context(scope: MockRequestHandleScope)
    suspend fun handler(request: HttpRequestData): HttpResponseData {
        return with(scope) {
            when (request.url.encodedPath) {
                "/api/auth/login" -> {
                    if (loginResponseData != null) {
                        respond(
                            content = loginResponseData!!.content,
                            status = loginResponseData!!.status,
                            headers = headersOf(
                                "Content-Type", listOf("application/json")
                            )
                        )
                    } else {
                        respondOk("")
                    }
                }

                "/api/chats/sync" -> {
                    if (syncResponseData != null) {
                        respond(
                            content = syncResponseData!!.content,
                            status = syncResponseData!!.status,
                            headers = headers {
                                append("Content-Type", "application/json")
                            }
                        )
                    } else respondOk("")
                }

                "/api/chats/receipt" -> {
                    if (receiptResponseData != null) {
                        respond(
                            content = receiptResponseData!!.content,
                            status = receiptResponseData!!.status,
                            headers = headers {
                                append("Content-Type", "application/json")
                            }
                        )
                    } else {
                        respondOk("")
                    }
                }

                else -> {
                    println("Responding Error")
                    respondError(HttpStatusCode.NotFound)
                }
            }
        }
    }
}

data class ResponseData(
    val content: String,
    val status: HttpStatusCode,
    val headers: Map<String, String> = mapOf()
)