package com.techullurgy.howzapp.queries

import com.techullurgy.howzapp.chats.infra.database.entities.*
import com.techullurgy.howzapp.chats.infra.database.entities.chats.OneToOneChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.messages.TextMessageEntity
import com.techullurgy.howzapp.chats.infra.database.projections.ChatMessageProjection
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageRepository
import com.techullurgy.howzapp.chats.infra.utils.JPQLQueries
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.common.types.id
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.uuid.Uuid

@DataJpaTest
class JPQLQueriesTest {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var repo: ChatMessageRepository

    private lateinit var users: List<UserEntity>
    private lateinit var chats: List<ChatEntity>
    private lateinit var messages: List<ChatMessageEntity>

    @BeforeTest
    fun setup() {
        users = List(4) {
            UserEntity(UserId.id, "Irsath-$it", "", "", "")
        }.also { it.forEach { e -> em.persist(e) } }

        chats = List(2) {
            when (it) {
                0 -> OneToOneChatEntity(originator = users[0], participant = users[1])
                1 -> OneToOneChatEntity(originator = users[0], participant = users[2])
                else -> TODO()
            }
        }.also { it.forEach { e -> em.persist(e) } }

        messages = listOf(
            TextMessageEntity(
                sender = users[0],
                belongsToChat = chats[0],
                text = "Message-00",
                createdAt = Instant.now().plusSeconds(1),
            ),
            TextMessageEntity(
                sender = users[1],
                belongsToChat = chats[0],
                text = "Message-11",
                createdAt = Instant.now().plusSeconds(2),
            ),
            TextMessageEntity(
                sender = users[0],
                belongsToChat = chats[0],
                text = "Message-30",
                createdAt = Instant.now().plusSeconds(3),
            ),
            TextMessageEntity(
                sender = users[1],
                belongsToChat = chats[0],
                text = "Message-41",
                createdAt = Instant.now().plusSeconds(4),
            ),
            TextMessageEntity(
                sender = users[0],
                belongsToChat = chats[0],
                text = "Message-50",
                createdAt = Instant.now().plusSeconds(5),
            ),
            TextMessageEntity(
                sender = users[0],
                belongsToChat = chats[0],
                text = "Message-60",
                createdAt = Instant.now().plusSeconds(6),
            ),

            TextMessageEntity(
                sender = users[0],
                belongsToChat = chats[1],
                text = "Message-00",
                createdAt = Instant.now().plusSeconds(7),
            ),
            TextMessageEntity(
                sender = users[2],
                belongsToChat = chats[1],
                text = "Message-12",
                createdAt = Instant.now().plusSeconds(8),
            ),
            TextMessageEntity(
                sender = users[0],
                belongsToChat = chats[1],
                text = "Message-30",
                createdAt = Instant.now().plusSeconds(9),
            ),
            TextMessageEntity(
                sender = users[2],
                belongsToChat = chats[1],
                text = "Message-42",
                createdAt = Instant.now().plusSeconds(10),
            ),
            TextMessageEntity(
                sender = users[0],
                belongsToChat = chats[1],
                text = "Message-50",
                createdAt = Instant.now().plusSeconds(11),
            ),
            TextMessageEntity(
                sender = users[0],
                belongsToChat = chats[1],
                text = "Message-60",
                createdAt = Instant.now().plusSeconds(12),
            ),
        ).also { it.forEach { e -> em.persist(e) } }

        messages.map {
            ChatMessageStatusEntity(message = it, sender = it.sender, status = MessageStatus.SENT)
        }.mapIndexed { index, s ->
            if (index == 5) {
                ChatMessageStatusEntity(message = s.message, sender = s.sender, status = MessageStatus.RECEIVED)
            } else s
        }.also { it.forEach { e -> em.persist(e) } }

        em.flush()
    }

    @Test
    fun `load messages with user status`() {

        val queryResult1 =
            em.createQuery(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.QUERY, ChatMessageProjection::class.java)
            .setParameter(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.PARAM_USER, users[0])
                .setParameter(
                    JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.PARAM_MESSAGE_AFTER,
                    Instant.now().minusSeconds(5)
                )
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

        val queryResult2 =
            em.createQuery(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.QUERY, ChatMessageProjection::class.java)
            .setParameter(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.PARAM_USER, users[1])
                .setParameter(
                    JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.PARAM_MESSAGE_AFTER,
                    Instant.now().minusSeconds(5)
                )
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

    @Test
    fun `load before message in the chat`() {
        val resultList =
            em.createQuery(JPQLQueries.LOADING_SLICE_OF_MESSAGES_FOR_CHAT.QUERY, ChatMessageProjection::class.java)
                .setParameter(JPQLQueries.LOADING_SLICE_OF_MESSAGES_FOR_CHAT.PARAM_CHAT, chats[0].id)
                .setParameter(JPQLQueries.LOADING_SLICE_OF_MESSAGES_FOR_CHAT.PARAM_REQUESTER, users[1].id)
                .setParameter(JPQLQueries.LOADING_SLICE_OF_MESSAGES_FOR_CHAT.PARAM_BEFORE_MESSAGE, messages[3].id)
                .resultList

        assertEquals(3, resultList.size)
    }

    @Test
    fun `load multiple receipts for same user, message, chat`() {

        ChatMessageReceiptsEntity("asdjaksh", messages[0], users[0], Receipt.PENDING).also {
            em.persist(it)
        }
        ChatMessageReceiptsEntity("asdgaksh", messages[1], users[0], Receipt.READ).also {
            em.persist(it)
        }

        em.flush()

        val result =
            em.createQuery(JPQLQueries.GET_RECEIPTS_FOR_MESSAGE_FOR_USER.QUERY, ChatMessageReceiptsEntity::class.java)
                .setParameter(JPQLQueries.GET_RECEIPTS_FOR_MESSAGE_FOR_USER.PARAM_USER_ID, users[0].id)
                .setParameter(JPQLQueries.GET_RECEIPTS_FOR_MESSAGE_FOR_USER.PARAM_MESSAGE_ID, messages[1].id)
                .resultList
                .first()

//        assertEquals("asdjaksh", result.id)
//        assertEquals(Receipt.PENDING, result.receipt)

        assertEquals("asdgaksh", result.id)
        assertEquals(Receipt.READ, result.receipt)
    }
}

private data class Projection(
    val message: ChatMessageEntity,
    val status: ChatMessageStatusEntity?
)