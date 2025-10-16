package com.techullurgy.howzapp.chats.infra.database.repositories

import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageReceiptsEntity
import com.techullurgy.howzapp.chats.infra.database.entities.Receipt
import com.techullurgy.howzapp.chats.infra.utils.JPQLQueries
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ChatMessageReceiptsRepository : JpaRepository<ChatMessageReceiptsEntity, String> {

    @Modifying
    @Query(JPQLQueries.UPDATE_RECEIPT_FOR_MESSAGE_AND_FOR_USER.QUERY)
    fun updateReceiptFor(
        @Param(JPQLQueries.UPDATE_RECEIPT_FOR_MESSAGE_AND_FOR_USER.PARAM_USER_ID) userId: UserId,
        @Param(JPQLQueries.UPDATE_RECEIPT_FOR_MESSAGE_AND_FOR_USER.PARAM_MESSAGE_ID) messageId: MessageId,
        @Param(JPQLQueries.UPDATE_RECEIPT_FOR_MESSAGE_AND_FOR_USER.PARAM_RECEIPT) receipt: Receipt,
        @Param(JPQLQueries.UPDATE_RECEIPT_FOR_MESSAGE_AND_FOR_USER.PARAM_UPDATED_AT) updatedAt: Instant = Instant.now(),
    )

    @Query(JPQLQueries.CAN_UPDATE_DELIVERED_STATUS_OF_MESSAGE.QUERY)
    fun hasAllAtLeastReceiptForMessage(
        @Param(JPQLQueries.CAN_UPDATE_DELIVERED_STATUS_OF_MESSAGE.PARAM_MESSAGE_ID) messageId: MessageId,
        @Param(JPQLQueries.CAN_UPDATE_DELIVERED_STATUS_OF_MESSAGE.PARAM_RECEIPT) receipt: Receipt
    ): Boolean
}