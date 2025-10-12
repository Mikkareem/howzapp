package com.techullurgy.howzapp.chats.infra.database.repositories

import com.techullurgy.howzapp.chats.infra.database.entities.ChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.chats.infra.utils.JPQLQueries
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.users.infra.database.entities.UserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessageEntity, MessageId> {

    @Query(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.QUERY)
    fun fetchNewMessagesForUser(
        @Param(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.PARAM_USER) user: UserEntity,
        @Param(JPQLQueries.FETCH_NEW_MESSAGES_FOR_USER.PARAM_LAST_SYNCED_TIME) lastSyncedTime: Instant,
    ): List<ChatMessageEntity>

    @Query(JPQLQueries.LOADING_SLICE_OF_MESSAGES_FOR_CHAT.QUERY)
    fun loadChunkOfMessagesFromChat(
        @Param(JPQLQueries.LOADING_SLICE_OF_MESSAGES_FOR_CHAT.PARAM_REQUESTER) requester: UserEntity,
        @Param(JPQLQueries.LOADING_SLICE_OF_MESSAGES_FOR_CHAT.PARAM_CHAT) chat: ChatEntity,
        @Param(JPQLQueries.LOADING_SLICE_OF_MESSAGES_FOR_CHAT.PARAM_BEFORE_CREATED_AT) lastInstant: Instant,
        pageable: Pageable
    ): Slice<ChatMessageEntity>
}