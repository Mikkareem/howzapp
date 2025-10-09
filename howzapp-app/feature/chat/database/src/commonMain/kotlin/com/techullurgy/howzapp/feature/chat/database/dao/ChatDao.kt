package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.techullurgy.howzapp.feature.chat.database.entities.ChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatMessageStatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantCrossRef
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.database.relations.ChatDetails
import com.techullurgy.howzapp.feature.chat.database.relations.ChatInfoWithLastMessageRelation
import com.techullurgy.howzapp.feature.chat.database.views.ChatMessageView
import com.techullurgy.howzapp.feature.chat.database.views.ChatPendingMessagesView
import com.techullurgy.howzapp.feature.chat.database.views.ChatPendingUploadsSuccessView
import com.techullurgy.howzapp.feature.chat.database.views.ChatPendingUploadsTriggeredView
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Upsert
    suspend fun upsertChat(chat: ChatEntity)

    @Upsert
    suspend fun upsertMessage(message: ChatMessageEntity): Long

    @Upsert
    suspend fun upsertMessageStatus(message: ChatMessageStatusEntity)

    @Upsert
    suspend fun upsertParticipant(participant: ChatParticipantEntity)

    @Upsert
    suspend fun upsertParticipantCrossRef(participant: ChatParticipantCrossRef)

    @Transaction
    @Query("SELECT * FROM view__chat_info_of_the_chats WHERE chatId = :chatId")
    fun observeChatDetailsById(chatId: String): Flow<ChatDetails?>

    @Transaction
    @Query("SELECT * FROM view__chat_info_of_the_chats")
    fun observeChatInfos(): Flow<List<ChatInfoWithLastMessageRelation>>

    @Transaction
    @Query("SELECT * FROM view__pending_messages_on_all_the_chats")
    fun observePendingMessagesThatAreReadyToSync(): Flow<List<ChatPendingMessagesView>>

    @Transaction
    @Query("SELECT * FROM view__pending_uploads_on_chats_that_are_triggered")
    fun observeUploadablePendingMessageTriggered(): Flow<List<ChatPendingUploadsTriggeredView>>

    @Transaction
    @Query("SELECT * FROM view__pending_uploads_on_chats_that_are_complete_successfully")
    fun observeUploadablePendingMessageCompletedSuccess(): Flow<List<ChatPendingUploadsSuccessView>>

    @Query(
        """
            UPDATE ChatMessageEntity
            SET messageId = :newMessageId, timestamp = :newTimestamp
            WHERE messageId = :oldMessageId
        """
    )
    suspend fun updateChatMessageAsSync(oldMessageId: String, newMessageId: String, newTimestamp: Long)

    @Transaction
    suspend fun upsertParticipantToTheChat(chatId: String, participant: ChatParticipantEntity) {
        upsertParticipant(participant)
        upsertParticipantCrossRef(ChatParticipantCrossRef(chatId, participant.userId))
    }
    @Transaction
    @Query("SELECT * FROM view__chat_info_of_the_chats WHERE chatId = :chatId")
    suspend fun getChatDetailsById(chatId: String): ChatDetails?

    @Transaction
    @Query("SELECT * FROM view__full_message WHERE messageId = :messageId")
    suspend fun getChatMessageEntityById(messageId: String): ChatMessageView?

    @Query("DELETE FROM ChatMessageEntity WHERE messageId = :messageId")
    suspend fun deleteMessagesById(messageId: String)

    @Query("DELETE FROM ChatParticipantCrossRef WHERE chatId = :chatId AND userId = :userId")
    suspend fun deleteParticipantsFromChat(chatId: String, userId: String)

    @Query("SELECT * FROM ChatParticipantEntity WHERE userId = :participantId")
    suspend fun getParticipantById(participantId: String): ChatParticipantEntity?
}