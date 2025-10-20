package com.techullurgy.howzapp.feature.chat.presentation.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatInfo
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEvent
import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEventType
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
import com.techullurgy.howzapp.feature.chat.presentation.screens.ConversationKey
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationViewModel
import com.techullurgy.howzapp.test.utilities.MainDispatcherRule
import com.techullurgy.howzapp.test.utilities.core.Notifier
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class ConversationViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val eventNotifier = Notifier<UserChatEvent>()
    private val chatNotifier = Notifier<Chat?>()

    private val mockedRepository = mockk<ChatRepository> {
        every { observeChatByChatId("c123") } returns chatNotifier.flow
        every { events } returns eventNotifier.flow
    }

    private lateinit var viewModel: ConversationViewModel

    @BeforeTest
    fun setup() {
        viewModel = ConversationViewModel(
            key = ConversationKey("c123"),
            chatRepository = mockedRepository
        )
    }

    @Test
    fun test_BasicUiState() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.state.collect() }

        chatNotifier.send(sampleDirectChat)

        assertThat(viewModel.state.value.title).isEqualTo("Joe Biden")
        assertThat(viewModel.state.value.subtitle).isEqualTo("")

        assertThat(viewModel.state.value.messageSheets.size).isEqualTo(2)
        assertThat(viewModel.state.value.messageSheets.first().isCurrentUser).isEqualTo(true)
        assertThat(viewModel.state.value.messageSheets.first().isPictureShowable).isEqualTo(true)
        assertThat(viewModel.state.value.messageSheets.last().isCurrentUser).isEqualTo(true)
        assertThat(viewModel.state.value.messageSheets.last().isPictureShowable).isEqualTo(false)

        verifyAll {
            mockedRepository.observeChatByChatId("c123")
            mockedRepository.events
        }
    }

    @Test
    fun test_OtherUserOnlineCaseWithNoUserChatEvents() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.state.collect() }

        chatNotifier.send(
            sampleDirectChat.copy(
                chatInfo = sampleDirectChat.chatInfo.copy(
                    chatType = (sampleDirectChat.chatInfo.chatType as ChatType.Direct).copy(
                        other = (sampleDirectChat.chatInfo.chatType as ChatType.Direct).other.copy(isOnline = true)
                    )
                )
            )
        )

        assertThat(viewModel.state.value.title).isEqualTo("Joe Biden")
        assertThat(viewModel.state.value.subtitle).isEqualTo("Online")
    }

    @Test
    fun test_OtherUserOnlineCaseWithUserChatEvents() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.state.collect() }

        chatNotifier.send(
            sampleDirectChat.copy(
                chatInfo = sampleDirectChat.chatInfo.copy(
                    chatType = (sampleDirectChat.chatInfo.chatType as ChatType.Direct).copy(
                        other = (sampleDirectChat.chatInfo.chatType as ChatType.Direct).other.copy(isOnline = true)
                    )
                )
            )
        )

        eventNotifier.send(
            UserChatEvent(
                "c123", "u456", UserChatEventType.TYPING
            )
        )

        assertThat(viewModel.state.value.title).isEqualTo("Joe Biden")
        assertThat(viewModel.state.value.subtitle).isEqualTo("typing...")

        advanceTimeBy(5000)
        assertThat(viewModel.state.value.subtitle).isEqualTo("typing...")

        advanceTimeBy(5001)
        assertThat(viewModel.state.value.subtitle).isEqualTo("Online")
    }
}

private val participants = listOf(
    ChatParticipant("u123", "John Doe"),
    ChatParticipant("u456", "Joe Biden"),
    ChatParticipant("u789", "Jesus Christ"),
    ChatParticipant("u345", "Panama Jung"),
)

private val sampleDirectChat = Chat(
    ChatInfo(
        "c123",
        ChatType.Direct(
            participants[0],
            participants[1]
        )
    ),
    listOf(
        ChatMessage(
            messageId = "m1",
            chatId = "c123",
            content = OriginalMessage.TextMessage("Hi, machi"),
            owner = MessageOwner.Me(owner = participants[0], status = MessageStatus.SenderStatus.DELIVERED),
            timestamp = Clock.System.now().minus(2.minutes + 3.seconds)
        ),
        ChatMessage(
            messageId = "m2",
            chatId = "c123",
            content = OriginalMessage.TextMessage("Hi, machi"),
            owner = MessageOwner.Me(owner = participants[0], status = MessageStatus.SenderStatus.DELIVERED),
            timestamp = Clock.System.now().minus(2.minutes + 1.seconds)
        )
    )
)