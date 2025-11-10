package com.techullurgy.howzapp.chats.infra.database.repositories

import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageStatusEntity
import com.techullurgy.howzapp.chats.infra.database.entities.MessageStatus
import com.techullurgy.howzapp.chats.infra.utils.JPQLQueries
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.MessageStatusId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ChatMessageStatusRepository : JpaRepository<ChatMessageStatusEntity, MessageStatusId> {

    @Modifying
    @Query(JPQLQueries.UPDATE_STATUS_OF_MESSAGE.QUERY)
    fun updateStatusForTheMessage(messageId: MessageId, status: MessageStatus, updatedAt: Instant = Instant.now())
}