package com.techullurgy.howzapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.techullurgy.howzapp.feature.chat.test.di.chatTestDomainModule
import com.techullurgy.howzapp.test.utilities.RobolectricTest
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.error_disk_full
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.jetbrains.compose.resources.getString
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.ksp.generated.configurationModules
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.robolectric.RuntimeEnvironment
import java.net.InetAddress
import kotlin.test.Test

class ComposeAppAndroidUnitTest : KoinTest, RobolectricTest() {

    @get:Rule
    val koin = KoinTestRule.create {
        androidContext(RuntimeEnvironment.getApplication())
        modules(DevelopmentApp.configurationModules + chatTestDomainModule)
    }

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun composeTest() {
        composeRule.setContent { App() }
        composeRule.onNodeWithText("Email").assertExists()
    }

    @Test
    fun anotherTest() {
        val expected = "Oops, it looks like your storage is full!"
        val actual = runBlocking { getString(Res.string.error_disk_full) }

        assertThat(actual).isEqualTo(expected)
    }
}

class SecondTest {

    @Test
    fun basicTest() = runTest {

        val client = HttpClient(CIO)

        val server = MockWebServer()

        server.start(
            inetAddress = InetAddress.getLoopbackAddress(),
            port = 8080
        )

        server.enqueue(MockResponse().setResponseCode(200).setBody("Hello Machans"))

        val response = client.get {
            url("http://localhost:8080/url")
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo("Hello Machans")

        server.close()
    }

    fun basicTest2() {
        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
            }
        }

        MockResponse()
            .withWebSocketUpgrade(webSocketListener)
    }
}