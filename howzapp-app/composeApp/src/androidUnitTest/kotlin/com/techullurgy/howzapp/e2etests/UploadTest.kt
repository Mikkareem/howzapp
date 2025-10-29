package com.techullurgy.howzapp.e2etests

import androidx.compose.ui.test.junit4.ComposeTestRule
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
import com.github.takahirom.roborazzi.captureRoboImage
import com.techullurgy.howzapp.App
import com.techullurgy.howzapp.DevelopmentApp
import com.techullurgy.howzapp.core.presentation.util.TestTag
import com.techullurgy.howzapp.core.presentation.util.loginEmailInput
import com.techullurgy.howzapp.core.presentation.util.loginPasswordInput
import com.techullurgy.howzapp.e2etests.responses.AppResponses
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.system.FileReader
import com.techullurgy.howzapp.feature.chat.test.di.chatTestDomainModule
import com.techullurgy.howzapp.test.utilities.AppMockEngine
import com.techullurgy.howzapp.test.utilities.MainDispatcherRule
import com.techullurgy.howzapp.test.utilities.ResponseData
import com.techullurgy.howzapp.test.utilities.RobolectricTest
import com.techullurgy.howzapp.test.utilities.testModule
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.login
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.getString
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.ksp.generated.configurationModules
import org.koin.test.KoinTestRule
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import kotlin.test.BeforeTest
import kotlin.test.Test

@Config(
    qualifiers = "w412dp-h924dp-420dpi",
)
@LooperMode(LooperMode.Mode.PAUSED)
class UploadTest: RobolectricTest() {

    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule(order = 2)
    val koinRule = KoinTestRule.create {
        androidContext(RuntimeEnvironment.getApplication())
        modules(
            DevelopmentApp.configurationModules + chatTestDomainModule + testModule + module {
                single<FileReader> {
                    mockk<FileReader>() {
                        coEvery { getFileBytesFromUrl(any()) } returns "repeat".repeat(20000).toByteArray()
                    }
                }
            }
        )
    }

    @get:Rule(order = 3)
    val composeTestRule = createComposeRule()

    private val json: Json = Json

    @BeforeTest
    fun setup() {
        composeTestRule.setContent { App() }
    }

    @Test
    fun uploadTest() = runTest {
        val appServer = koinRule.koin.get<AppMockEngine>()

        val loginResponse = AppResponses.loginResponse
        val syncResponse = AppResponses.syncResponse

        appServer.apply {
            loginResponseData = ResponseData(
                content = json.encodeToString(loginResponse),
                status = HttpStatusCode.OK
            )
            syncResponseData = ResponseData(
                content = json.encodeToString(syncResponse),
                status = HttpStatusCode.OK
            )
        }

        composeTestRule.apply {
            onNodeWithTag(TestTag.loginEmailInput.name)
                .assertExists()
                .performTextInput("irsath.kareem@howzapp.com")

            onNodeWithTag(TestTag.loginPasswordInput.name)
                .assertExists()
                .performTextInput("Irka@123")

            onNodeWithText(getString(Res.string.login))
                .assertExists().performClick()

            advanceUntilIdle()

            waitUntil {
                onAllNodesWithText("Riyas").fetchSemanticsNodes().isNotEmpty()
            }
        }

        val repository = koinRule.koin.get<ChatLocalRepository>()

        repository.newPendingMessage(
            chatId = syncResponse.chats.first().messages.first().chatId,
            senderId = loginResponse.id,
            message = PendingMessage.UploadablePendingMessage(
                status = UploadStatus.Triggered(),
                originalMessage = OriginalMessage.ImageMessage("89")
            )
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            repository.observeConversation(chatId = syncResponse.chats.first().messages.first().chatId).collect {
                it?.let { it.chatMessages.filter { c -> c.content is PendingMessage.UploadablePendingMessage }.forEach { v -> println(v) } }
            }
        }

        advanceUntilIdle()

        composeTestRule.waitForIdle()

        composeTestRule.captureImageNow()
    }

    @OptIn(ExperimentalRoborazziApi::class)
    private fun ComposeTestRule.captureImageNow() {
        onRoot()
            .captureRoboImage(
                roborazziOptions = RoborazziOptions(taskType = RoborazziTaskType.Record)
            )
    }
}