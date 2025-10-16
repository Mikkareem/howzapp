package com.techullurgy.howzapp.services

import com.techullurgy.howzapp.chats.api.dto.MessageDto
import com.techullurgy.howzapp.chats.api.dto.NewMessageRequest
import com.techullurgy.howzapp.chats.infra.database.entities.MessageStatus
import com.techullurgy.howzapp.chats.infra.database.entities.Receipt
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageRepository
import com.techullurgy.howzapp.chats.models.TextMessage
import com.techullurgy.howzapp.chats.services.ChatService
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import io.mockk.mockk
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.uuid.Uuid

@SpringBootTest
@Import(RabbitMockConfig::class)
class ChatServiceTests {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var chatService: ChatService

    @Autowired
    private lateinit var chatMessageRepository: ChatMessageRepository


    @Test
    @Transactional
    fun testBasic() {
        val user1 = UserEntity(Uuid.id.toString(), "Irsath", "", "", "")
        val user2 = UserEntity(Uuid.id.toString(), "Kareem", "", "", "")

        em.persist(user1)
        em.persist(user2)

        chatService.onNewMessage(
            from = user1.id,
            chatId = listOf(user1.id, user2.id).sorted().joinToString("__"),
            message = MessageDto.TextMessageDto("Message 1")
        )

        val newMessagesForUser1 = chatMessageRepository.fetchNewMessagesForUser(user1, Instant.now().minusSeconds(10))

        assertEquals(1, newMessagesForUser1.size)
        assertNull(newMessagesForUser1.first().receipt)
        assertNotNull(newMessagesForUser1.first().status)

        val newMessagesForUser2 = chatMessageRepository.fetchNewMessagesForUser(user2, Instant.now().minusSeconds(10))

        assertEquals(1, newMessagesForUser2.size)
        assertNull(newMessagesForUser2.first().status)
        assertNotNull(newMessagesForUser2.first().receipt)

    }

    @Test
    @Transactional
    fun testBasic2() {
        val user1 = UserEntity(Uuid.id.toString(), "Irsath", "", "", "")
        val user2 = UserEntity(Uuid.id.toString(), "Kareem", "", "", "")
        val user3 = UserEntity(Uuid.id.toString(), "Riyas", "", "", "")

        em.persist(user1)
        em.persist(user2)
        em.persist(user3)

        chatService.onNewMessage(
            from = user1.id,
            chatId = listOf(user1.id, user2.id).sorted().joinToString("__"),
            message = MessageDto.TextMessageDto("Message 1-2")
        )

        chatService.onNewMessage(
            from = user1.id,
            chatId = listOf(user1.id, user3.id).sorted().joinToString("__"),
            message = MessageDto.TextMessageDto("Message 1-3")
        )

        chatService.onNewMessage(
            from = user3.id,
            chatId = listOf(user3.id, user2.id).sorted().joinToString("__"),
            message = MessageDto.TextMessageDto("Message 3-2")
        )

        chatService.onNewMessage(
            from = user2.id,
            chatId = listOf(user3.id, user2.id).sorted().joinToString("__"),
            message = MessageDto.TextMessageDto("Message 2-3")
        )

        val user2Chats = chatService.loadNewMessagesForUser(user2.id, Instant.now().minusSeconds(20))

        assertEquals(2, user2Chats.size)

        assertEquals(
            2,
            user2Chats.first { it.chatType.id == listOf(user3.id, user2.id).sorted().joinToString("__") }
                .messages.size
        )

        assertNull(
            user2Chats.first { it.chatType.id == listOf(user3.id, user2.id).sorted().joinToString("__") }
                .messages.first { (it.message as TextMessage).text == "Message 2-3" }
                .receipt
        )
        assertNotNull(
            user2Chats.first { it.chatType.id == listOf(user3.id, user2.id).sorted().joinToString("__") }
                .messages.first { (it.message as TextMessage).text == "Message 2-3" }
                .status
        )
    }

    @Test
    @Transactional
    fun testBasic3() {
        val user1 = UserEntity(Uuid.id.toString(), "Irsath", "", "", "")
        val user2 = UserEntity(Uuid.id.toString(), "Kareem", "", "", "")
        val user3 = UserEntity(Uuid.id.toString(), "Riyas", "", "", "")

        em.persist(user1)
        em.persist(user2)
        em.persist(user3)

        val participants1 = listOf(user1.id, user2.id)
        List(120) {
            val chatId = participants1.sorted().joinToString("__")
            val sender = participants1.random()

            sender to NewMessageRequest(
                chatId = chatId,
                localMessageId = MessageId.id,
                message = MessageDto.TextMessageDto("Message - $it from user${participants1.indexOfFirst { it == sender } + 1}")
            )
        }.forEach {
            chatService.onNewMessage(it.first, it.second.chatId, it.second.message)
            Thread.sleep(15)
        }

        val user2Chats = chatService.loadNewMessagesForUser(user2.id, Instant.now().minusSeconds(20))

        assertEquals(1, user2Chats.size)

        val chatMessages = user2Chats.first().messages.sortedByDescending { it.timestamp }

        assertEquals(120, chatMessages.size)

        val returnedIds = chatService.loadMessagesFromChatBefore(
            user2.id,
            participants1.sorted().joinToString("__"),
            chatMessages[5].message.id
        ).sortedByDescending { it.timestamp }.map { it.message.id.toString() }

        val expectedIds = chatMessages.drop(6).take(20).map { it.message.id.toString() }

        assertEquals(expectedIds, returnedIds)
    }

    @Test
    @Transactional
    fun testBasic4() {
        val user1 = UserEntity(Uuid.id.toString(), "Irsath", "", "", "")
        val user2 = UserEntity(Uuid.id.toString(), "Kareem", "", "", "")
        val user3 = UserEntity(Uuid.id.toString(), "Riyas", "", "", "")

        em.persist(user1)
        em.persist(user2)
        em.persist(user3)

        val participants1 = listOf(user1.id, user2.id)
        List(120) {
            val chatId = participants1.sorted().joinToString("__")
            val sender = participants1.random()

            sender to NewMessageRequest(
                chatId = chatId,
                localMessageId = MessageId.id,
                message = MessageDto.TextMessageDto("Message - $it from user${participants1.indexOfFirst { it == sender } + 1}")
            )
        }.forEach {
            chatService.onNewMessage(it.first, it.second.chatId, it.second.message)
            Thread.sleep(15)
        }

        val user2Chats1 = chatService.loadNewMessagesForUser(user2.id, Instant.now().minusSeconds(20))

        val message = user2Chats1.first().messages.first { it.sender.id == user2.id }

        assertEquals(MessageStatus.SENT, message.status)

        chatService.updateMessageReceipt(
            user1.id,
            message.message.id,
            Receipt.DELIVERED
        )

        // Needed because, already targetedMessage is loaded in Persistence Context. Need to clear those
        // in order to query again
        em.clear()

        val user2Chats2 = chatService.loadNewMessagesForUser(user2.id, Instant.now().minusSeconds(20))

        val targetedMessage = user2Chats2.first().messages.first { it.message.id == message.message.id }

        assertEquals(MessageStatus.RECEIVED, targetedMessage.status)
    }

    @Test
    @Transactional
    fun testBasic5() {
        val user1 = UserEntity(Uuid.id.toString(), "Irsath", "", "", "")
        val user2 = UserEntity(Uuid.id.toString(), "Kareem", "", "", "")
        val user3 = UserEntity(Uuid.id.toString(), "Riyas", "", "", "")

        em.persist(user1)
        em.persist(user2)
        em.persist(user3)

        val participants1 = listOf(user1.id, user2.id)
        List(120) {
            val chatId = participants1.sorted().joinToString("__")
            val sender = participants1.random()

            sender to NewMessageRequest(
                chatId = chatId,
                localMessageId = MessageId.id,
                message = MessageDto.TextMessageDto("Message - $it from user${participants1.indexOfFirst { it == sender } + 1}")
            )
        }.forEach {
            chatService.onNewMessage(it.first, it.second.chatId, it.second.message)
            Thread.sleep(15)
        }

        val user2Chats = chatService.loadNewMessagesForUser(user2.id, Instant.now().minusSeconds(20))

        val sorted = user2Chats.first().messages.sortedByDescending { it.timestamp }

        val ids = sorted.take(40).map { it.message.id }

        val rSorted = chatService.loadMessagesByIdsForUser(user2.id, ids).sortedByDescending { it.timestamp }

        assertEquals(ids.size, rSorted.size)
    }
}

@TestConfiguration
internal class RabbitMockConfig {

    @Bean
    @Primary
    fun provideMockRabbitTemplate(): RabbitTemplate {
        return mockk(relaxed = true)
    }
}