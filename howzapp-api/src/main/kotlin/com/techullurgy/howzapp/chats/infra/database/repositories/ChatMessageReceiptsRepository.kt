package com.techullurgy.howzapp.chats.infra.database.repositories

import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageReceiptsEntity
import com.techullurgy.howzapp.chats.infra.utils.JPQLQueries
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageReceiptsRepository : JpaRepository<ChatMessageReceiptsEntity, String> {

    @Query(JPQLQueries.GET_RECEIPTS_FOR_MESSAGE_FOR_USER.QUERY)
    fun findByUserIdAndMessageId(
        @Param(JPQLQueries.GET_RECEIPTS_FOR_MESSAGE_FOR_USER.PARAM_USER_ID) userId: UserId,
        @Param(JPQLQueries.GET_RECEIPTS_FOR_MESSAGE_FOR_USER.PARAM_MESSAGE_ID) messageId: MessageId
    ): ChatMessageReceiptsEntity
}