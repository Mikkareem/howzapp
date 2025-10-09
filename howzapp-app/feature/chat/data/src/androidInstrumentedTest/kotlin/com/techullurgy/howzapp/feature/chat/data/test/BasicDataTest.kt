package com.techullurgy.howzapp.feature.chat.data.test

import com.techullurgy.howzapp.core.di.coreModule
import com.techullurgy.howzapp.feature.chat.data.di.chatDataModule
import com.techullurgy.howzapp.feature.chat.database.DatabaseFactory
import com.techullurgy.howzapp.feature.chat.database.TestDatabaseFactory
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatInfo
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.test.utilities.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.koin.core.component.get
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock

class BasicDataTest: KoinTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @BeforeTest
    fun setup() {
        val testModule = module {
            single<DatabaseFactory> { TestDatabaseFactory(get()) }
        }
        loadKoinModules(listOf(chatDataModule, coreModule, testModule))
    }

    @Test
    fun testBasic() = runTest {
        get<ChatLocalRepository>().syncChats(chats)

        val chats = get<ChatLocalRepository>().observeChatInfoWithLastMessage().first()
        val chat = get<ChatLocalRepository>().observeChatDetailsById("c123").first()

        println(chat)

        assertEquals(1, chats.size)
        assertEquals("c123", chat?.chatInfo?.chatId)
    }
}

private val chats = listOf(
    Chat(
        chatInfo = ChatInfo(
            chatId = "c123",
            chatType = ChatType.Group("Important Chat", ""),
            originator = ChatParticipant(userId = "u123", "Irsath")
        ),
        chatMessages = listOf(
            ChatMessage(
                messageId = "m123",
                chatId = "c123",
                content = Message.NonUploadablePendingMessage(
                    Message.TextMessage("Hi, How are you?"),
                ),
                owner = MessageOwner.Me(
                    owner = ChatParticipant(userId = "u123", "Irsath"),
                    msgStatus = MessageStatus.DELIVERED
                ),
                timestamp = Clock.System.now()
            )
        ),
        chatParticipants = listOf(
            ChatParticipant(userId = "u123", "Irsath"),
            ChatParticipant(userId = "u456", "Kareem")
        )
    )
)