package com.techullurgy.howzapp.basics

import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageStatusEntity
import com.techullurgy.howzapp.chats.infra.database.entities.MessageStatus
import com.techullurgy.howzapp.chats.infra.database.entities.chats.OneToOneChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.messages.TextMessageEntity
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageReceiptsRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageStatusRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatRepository
import com.techullurgy.howzapp.chats.infra.mappers.toDomain
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import com.techullurgy.howzapp.users.infra.database.repositories.UserRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.Uuid

@DataJpaTest
class PersistenceTests {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var chatRepository: ChatRepository

    @Autowired
    private lateinit var messageRepository: ChatMessageRepository

    @Autowired
    private lateinit var statusRepository: ChatMessageStatusRepository

    @Autowired
    private lateinit var receiptsRepository: ChatMessageReceiptsRepository

    @Test
    fun basicTest() {
        val savedUser = userRepository.saveAndFlush(
            UserEntity(UserId.id, "Irsath", "", "", "")
        )

        Thread.sleep(2000)

        val updatedUser = userRepository.saveAndFlush(
            savedUser.copy(email = "friend")
        )

        Thread.sleep(2000)

        userRepository.saveAndFlush(
            savedUser.copy(email = "friend 2")
        )

        Thread.sleep(2000)

        userRepository.saveAndFlush(
            savedUser.copy(email = "friend 5")
        )

        assertTrue(updatedUser.updatedAt.isAfter(updatedUser.createdAt))
        assertTrue(updatedUser.updatedAt.minusSeconds(6).isAfter(updatedUser.createdAt))
        assertFalse(updatedUser.updatedAt.minusSeconds(7).isAfter(updatedUser.createdAt))

        val updatedCount = em.createQuery("update UserEntity set email = ?1, updatedAt = ?2 where id = ?3")
            .setParameter(1, "good")
            .setParameter(2, Instant.now().plusSeconds(10))
            .setParameter(3, updatedUser.id)
            .executeUpdate()

        em.flush()
        em.clear()

        assertEquals(1, updatedCount)

        val up2 = userRepository.findById(updatedUser.id).get()

        assertTrue(up2.updatedAt.isAfter(up2.createdAt))
        assertTrue(up2.updatedAt.minusSeconds(16).isAfter(up2.createdAt))
        assertFalse(up2.updatedAt.minusSeconds(17).isAfter(up2.createdAt))
    }

    @Test
    @Transactional
    fun testBasic2() {
        val savedUsers = userRepository.saveAllAndFlush(
            listOf(
                UserEntity(UserId.id, "Irsath", "", "", ""),
                UserEntity(UserId.id, "Kareem", "", "", ""),
                UserEntity(UserId.id, "Faisal", "", "", ""),
            )
        )

        val savedChat = chatRepository.saveAndFlush(
            OneToOneChatEntity(originator = savedUsers[0], participant = savedUsers[1])
        )

        val messages = messageRepository.saveAllAndFlush(
            listOf(
                TextMessageEntity(sender = savedUsers[1], belongsToChat = savedChat, text = "Good morning"),
                TextMessageEntity(sender = savedUsers[0], belongsToChat = savedChat, text = "Good morning 2"),
                TextMessageEntity(sender = savedUsers[0], belongsToChat = savedChat, text = "Good morning 3"),
                TextMessageEntity(sender = savedUsers[1], belongsToChat = savedChat, text = "Good morning 4"),
            )
        )

        val statuses = statusRepository.saveAllAndFlush(
            messages.map {
                ChatMessageStatusEntity(message = it, status = MessageStatus.SENT, sender = it.sender)
            }
        )

        val lastSyncTime = Instant.now().plusSeconds(2)

        Thread.sleep(4000)

        statusRepository.updateStatusForTheMessage(messages[2].id, MessageStatus.RECEIVED)

        em.flush()
        em.clear()

        val status = statusRepository.findById(statuses[2].id).get()

        assertTrue(statuses[2].updatedAt.isBefore(status.updatedAt))
        assertTrue(statuses[2].updatedAt.plusSeconds(4).isBefore(status.updatedAt))
        assertFalse(statuses[2].updatedAt.plusSeconds(5).isBefore(status.updatedAt))

        val newMessages = messageRepository.fetchNewMessagesForUser(savedUsers[0], lastSyncTime).map { it.toDomain() }

        println(newMessages)
    }
}