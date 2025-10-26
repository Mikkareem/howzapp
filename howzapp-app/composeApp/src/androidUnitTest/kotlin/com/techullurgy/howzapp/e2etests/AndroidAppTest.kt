package com.techullurgy.howzapp.e2etests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziTaskType
import com.github.takahirom.roborazzi.captureRoboGif
import com.techullurgy.howzapp.App
import com.techullurgy.howzapp.DevelopmentApp
import com.techullurgy.howzapp.core.data.di.HostAndPort
import com.techullurgy.howzapp.core.dto.models.AuthInfoSerializable
import com.techullurgy.howzapp.core.dto.models.ChatDto
import com.techullurgy.howzapp.core.dto.models.ChatMessageDto
import com.techullurgy.howzapp.core.dto.models.DirectChatDto
import com.techullurgy.howzapp.core.dto.models.MessageStatusDto
import com.techullurgy.howzapp.core.dto.models.ReceiptDto
import com.techullurgy.howzapp.core.dto.models.TextMessageDto
import com.techullurgy.howzapp.core.dto.models.UserDto
import com.techullurgy.howzapp.core.dto.responses.SyncResponse
import com.techullurgy.howzapp.core.presentation.util.TestTag
import com.techullurgy.howzapp.core.presentation.util.loginEmailInput
import com.techullurgy.howzapp.core.presentation.util.loginPasswordInput
import com.techullurgy.howzapp.feature.chat.test.di.chatTestDomainModule
import com.techullurgy.howzapp.test.utilities.MainDispatcherRule
import com.techullurgy.howzapp.test.utilities.RobolectricTest
import com.techullurgy.howzapp.test.utilities.testModule
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import org.koin.ksp.generated.configurationModules
import org.koin.test.KoinTestRule
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.net.InetAddress
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@Config(qualifiers = "w480dp-h700dp-480dpi")
class AndroidAppTest : RobolectricTest() {

    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule(order = 2)
    val koinRule = KoinTestRule.create {
        androidContext(RuntimeEnvironment.getApplication())
        modules(
            DevelopmentApp.configurationModules + chatTestDomainModule + testModule + module {
                single<String>(qualifier = qualifier<HostAndPort>()) { "localhost:8080" }
            }
        )
    }

    @get:Rule(order = 3)
    val composeTestRule = createComposeRule()

    private lateinit var server: MockWebServer
    private lateinit var json: Json

    @BeforeTest
    fun setup() {
        json = Json
        server = MockWebServer()
        server.start(
            inetAddress = InetAddress.getLoopbackAddress(),
            port = 8080
        )

        composeTestRule.setContent { App() }
    }

    @AfterTest
    fun close() {
        server.shutdown()
    }

    @OptIn(ExperimentalTime::class, ExperimentalRoborazziApi::class)
    @Test
    fun `login screen is the first screen to show if no user is logged in already`() = runTest {

        val loggedInUserId = "9a8asocjaosd0as9d00aca09sd"
        val otherUserId = "9a8sda98c9a8s7d9as87dasdap"

        val loginResponse = AuthInfoSerializable(
            accessToken = "aisudiausdy",
            refreshToken = "kajsdhkajsdh",
            id = loggedInUserId
        )
        val syncResponse = SyncResponse(
            chats = listOf(
                ChatDto(
                    chatType = DirectChatDto(
                        chatId = listOf(loggedInUserId, otherUserId).sorted().joinToString("__"),
                        participant1 = UserDto(loggedInUserId, "Irsath", ""),
                        participant2 = UserDto(otherUserId, "Riyas", "")
                    ),
                    messages = listOf(
                        ChatMessageDto(
                            messageId = "asda0s9dosada0s98da09sd809as8d",
                            chatId = listOf(loggedInUserId, otherUserId).sorted()
                                .joinToString("__"),
                            message = TextMessageDto("Who are you?"),
                            sender = UserDto(userId = loggedInUserId, name = "Irsath", ""),
                            status = MessageStatusDto.SENT,
                            receipt = null,
                            timestamp = Clock.System.now().minus(3.seconds)
                        ),
                        ChatMessageDto(
                            messageId = "asda0s9dosada0sasdasd87as7d7as",
                            chatId = listOf(loggedInUserId, otherUserId).sorted()
                                .joinToString("__"),
                            message = TextMessageDto("I am Riyas"),
                            sender = UserDto(userId = otherUserId, name = "Riyas", ""),
                            status = null,
                            receipt = ReceiptDto.DELIVERED,
                            timestamp = Clock.System.now().minus(5.seconds)
                        )
                    )
                )
            ),
            lastSyncTimestamp = Clock.System.now().toEpochMilliseconds()
        )

        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return with(json) {
                    with(request.requestUrl!!.encodedPath) {
                        when {
                            startsWith("/api/auth/login") -> {
                                getMockResponse(loginResponse)
                            }

                            startsWith("/api/chats/sync") -> {
                                getMockResponse(syncResponse)
                            }

                            startsWith("/ws") -> {
                                MockResponse().withWebSocketUpgrade(object : WebSocketListener() {})
                            }

                            else -> MockResponse().setResponseCode(404)
                        }
                    }
                }
            }
        }

        composeTestRule.apply {
            onRoot()
                .captureRoboGif(
                    this, "gifs/1.gif", roborazziOptions = RoborazziOptions(
                        taskType = RoborazziTaskType.Record,
                        // Pixel-perfect matching
                        compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0f),
                        // Reduce the size of the PNGs
                        recordOptions = RoborazziOptions.RecordOptions(resizeScale = 1.0),
                    )
                ) {
                    onNodeWithTag(TestTag.loginEmailInput.name)
                        .assertExists()
                        .performTextInput("irsath.kareem@howzapp.com")

                    onNodeWithTag(TestTag.loginPasswordInput.name)
                        .assertExists()
                        .performTextInput("Irka@123")

//                    onRoot()
//                        .captureRoboImage(
//                            filePath = "screenshots/1.png",
//                            roborazziOptions = RoborazziOptions(
//                                // Don't pass taskType here, provide using gradle task or gradle property
//                                // recordRoborazziDebug, verifyRoborazziDebug
//                                taskType = RoborazziTaskType.Record,
//                                compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0f),
//                                recordOptions = RoborazziOptions.RecordOptions(resizeScale = 1.0),
//                            )
//                        )

                    onNodeWithText("Log In")
                        .assertExists().performClick()

                    advanceUntilIdle()

                    waitUntil {
                        onAllNodesWithText("Riyas").fetchSemanticsNodes().isNotEmpty()
                    }

//                    onRoot()
//                        .captureRoboImage(
//                            filePath = "screenshots/2.png",
//                            roborazziOptions = RoborazziOptions(
//                                taskType = RoborazziTaskType.Record,
//                                // Pixel-perfect matching
//                                compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0f),
//                                // Reduce the size of the PNGs
//                                recordOptions = RoborazziOptions.RecordOptions(resizeScale = 1.0),
//                            )
//                        )

                    onNodeWithText("Riyas").assertExists().performClick()

                    waitUntil {
                        onAllNodesWithText("Who are you?").fetchSemanticsNodes().isNotEmpty()
                    }

//                    onRoot()
//                        .captureRoboImage(
//                            filePath = "screenshots/3.png",
//                            roborazziOptions = RoborazziOptions(
//                                taskType = RoborazziTaskType.Record,
//                                // Pixel-perfect matching
//                                compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0f),
//                                // Reduce the size of the PNGs
//                                recordOptions = RoborazziOptions.RecordOptions(resizeScale = 1.0),
//                            )
//                        )
                }
        }
    }
}

context(json: Json)
inline fun <reified T> getMockResponse(body: T): MockResponse {
    return MockResponse()
        .addHeader("Content-Type", "application/json")
        .setBody(json.encodeToString(body))
}