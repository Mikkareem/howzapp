package com.techullurgy.howzapp.test.utilities

import com.techullurgy.howzapp.core.dto.models.AuthInfoSerializable
import com.techullurgy.howzapp.core.dto.responses.SyncResponse
import kotlinx.serialization.json.Json
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

context(json: Json)
inline fun <reified T> getMockResponse(body: T): MockResponse {
    return MockResponse()
        .addHeader("Content-Type", "application/json")
        .setBody(json.encodeToString(body))
}

class AbstractMockDispatcher(
    private val json: Json = Json
) : Dispatcher() {

    var loginResponse: AuthInfoSerializable? = null
    var syncResponse: SyncResponse? = null
    var wsListener: WebSocketListener? = null

    override fun dispatch(request: RecordedRequest): MockResponse {
        return with(json) {
            with(request.requestUrl!!.encodedPath) {
                when {
                    startsWith("/api/auth/login") -> {
                        if (loginResponse != null) {
                            getMockResponse(loginResponse)
                        } else MockResponse().setResponseCode(200)
                    }

                    startsWith("/api/chats/sync") -> {
                        if (syncResponse != null) {
                            getMockResponse(syncResponse)
                        } else MockResponse().setResponseCode(200)
                    }

                    startsWith("/ws") -> {
                        if (wsListener != null) {
                            MockResponse().withWebSocketUpgrade(wsListener!!)
                        } else {
                            MockResponse().withWebSocketUpgrade(object : WebSocketListener() {})
                        }
                    }

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
    }

    fun clear() {
        loginResponse = null
        syncResponse = null
        wsListener = null
    }
}