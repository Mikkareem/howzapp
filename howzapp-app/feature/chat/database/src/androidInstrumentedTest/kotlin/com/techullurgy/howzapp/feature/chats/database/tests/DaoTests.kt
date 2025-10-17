package com.techullurgy.howzapp.feature.chats.database.tests

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.techullurgy.howzapp.feature.chat.database.HowzappDatabase
import com.techullurgy.howzapp.feature.chat.database.entities.ChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.database.models.SerializableMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.time.Clock

class DaoTestes {

    lateinit var database: HowzappDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(
            context,
            HowzappDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    }

    @Test
    fun givenBasicChatWithPendingMessages_LoadsPendingMessagesIntoFlow() {
        val chatParticipants = List(10) {
            ChatParticipantEntity(
                userId = UUID.randomUUID().toString(),
                username = "Irsath - $it",
                onlineStatus = SerializableOnlineStatus.NoOnlineStatus,
                profilePictureUrl = "Irsath - $it Profile url"
            )
        }

        val chat1 = ChatEntity(
            chatId = UUID.randomUUID().toString(),
            type = SerializableChatType.Group(
                title = "Group Title",
                profileUrl = "Group Profile url"
            ),
            originator = chatParticipants.random().userId
        )

        val chat2 = ChatEntity(
            chatId = UUID.randomUUID().toString(),
            type = SerializableChatType.Group(
                title = "Group Title 2",
                profileUrl = "Group Profile url 2"
            ),
            originator = chatParticipants.random().userId
        )

        val chatMessages = List(100) {
            ChatMessageEntity(
                messageId = UUID.randomUUID().toString(),
                chatId = listOf(chat1, chat2).random().chatId,
                senderId = chatParticipants.random().userId,
                message = SerializableMessage.TextMessage("Good morning from $it"),
                timestamp = Clock.System.now().toEpochMilliseconds(),
                isRead = false
            )
        }

        runBlocking {
            database.chatDao.upsertChat(chat1)
            database.chatDao.upsertChat(chat2)
            chatParticipants.forEach {
                database.chatDao.upsertParticipantToTheChat(
                    chat1.chatId,
                    it
                )
            }
            chatMessages.forEach {
                println(database.chatDao.upsertMessage(it))
            }
            database.chatDao.observeChatInfos().first()
        }

        runBlocking {
            val chatRelation = database.chatDao.getChatDetailsById(chat1.chatId)
            val json = Json { prettyPrint = true }

            println(json.encodeToString(chatRelation))

            assertEquals(chatMessages.filter { it.chatId == chat1.chatId }.size, chatRelation?.messages?.count())
        }

        println("Completed")
        database.close()
    }
}