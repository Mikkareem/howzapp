package com.techullurgy.howzapp.queries

import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageStatusEntity
import com.techullurgy.howzapp.chats.infra.database.entities.MessageStatus
import com.techullurgy.howzapp.chats.infra.database.entities.chats.OneToOneChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.messages.TextMessageEntity
import com.techullurgy.howzapp.chats.infra.utils.JPQLQueries
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertContentEquals

@DataJpaTest
class JPQLQueriesTest {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Test
    fun `load messages with user status`() {
        val users = List(4) {
            UserEntity(UserId.id, "Irsath-$it", "")
        }.also { it.forEach { e -> em.persist(e) } }

        val chats = List(2) {
            when(it) {
                0 -> OneToOneChatEntity(originator = users[0], participant = users[1])
                1 -> OneToOneChatEntity(originator = users[0], participant = users[2])
                else -> TODO()
            }
        }.also { it.forEach { e -> em.persist(e) } }

        val messages: List<ChatMessageEntity> = listOf(
            TextMessageEntity(sender = users[0], belongsToChat = chats[0], text = "Message-00", createdAt = Instant.now().plusSeconds(1)),
            TextMessageEntity(sender = users[1], belongsToChat = chats[0], text = "Message-11", createdAt = Instant.now().plusSeconds(2)),
            TextMessageEntity(sender = users[0], belongsToChat = chats[0], text = "Message-30", createdAt = Instant.now().plusSeconds(3)),
            TextMessageEntity(sender = users[1], belongsToChat = chats[0], text = "Message-41", createdAt = Instant.now().plusSeconds(4)),
            TextMessageEntity(sender = users[0], belongsToChat = chats[0], text = "Message-50", createdAt = Instant.now().plusSeconds(5)),
            TextMessageEntity(sender = users[0], belongsToChat = chats[0], text = "Message-60", createdAt = Instant.now().plusSeconds(6)),

            TextMessageEntity(sender = users[0], belongsToChat = chats[1], text = "Message-00", createdAt = Instant.now().plusSeconds(7)),
            TextMessageEntity(sender = users[2], belongsToChat = chats[1], text = "Message-12", createdAt = Instant.now().plusSeconds(8)),
            TextMessageEntity(sender = users[0], belongsToChat = chats[1], text = "Message-30", createdAt = Instant.now().plusSeconds(9)),
            TextMessageEntity(sender = users[2], belongsToChat = chats[1], text = "Message-42", createdAt = Instant.now().plusSeconds(10)),
            TextMessageEntity(sender = users[0], belongsToChat = chats[1], text = "Message-50", createdAt = Instant.now().plusSeconds(11)),
            TextMessageEntity(sender = users[0], belongsToChat = chats[1], text = "Message-60", createdAt = Instant.now().plusSeconds(12)),
        ).also { it.forEach { e -> em.persist(e) } }

        messages.map {
            ChatMessageStatusEntity(message = it, sender = it.sender, status = MessageStatus.SENT)
        }.mapIndexed { index, s ->
            if(index == 5) {
                ChatMessageStatusEntity(message = s.message, sender = s.sender, status = MessageStatus.RECEIVED)
            } else s
        }.also { it.forEach { e -> em.persist(e) } }

        val queryResult1 = em.createQuery(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.QUERY, Projection::class.java)
            .setParameter(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.PARAM_USER, users[0])
            .setParameter(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.PARAM_LAST_SYNCED_TIME, Instant.now().minusSeconds(5))
            .resultList

        // Checking Correct Non-Null Value indices for user0
        assertContentEquals(
            listOf(0,2,4,5,6,8,10,11),
            queryResult1.reversed().mapIndexedNotNull { i,e ->
                if(e.status != null) i else null
            }
        )

        // Checking Correct Null value indices for user0
        assertContentEquals(
            listOf(1,3,7,9),
            queryResult1.reversed().mapIndexedNotNull { i,e ->
                if(e.status == null) i else null
            }
        )

        val queryResult2 = em.createQuery(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.QUERY, Projection::class.java)
            .setParameter(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.PARAM_USER, users[1])
            .setParameter(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.PARAM_LAST_SYNCED_TIME, Instant.now().minusSeconds(5))
            .resultList

        // for user1
        assertContentEquals(
            listOf(1,3),
            queryResult2.reversed().mapIndexedNotNull { i, e ->
                if(e.status != null) i else null
            }
        )

        // for user2
        assertContentEquals(
            listOf(0,2,4,5),
            queryResult2.reversed().mapIndexedNotNull { i, e ->
                if(e.status == null) i else null
            }
        )
    }
}

private data class Projection(
    val message: ChatMessageEntity,
    val status: ChatMessageStatusEntity?
)