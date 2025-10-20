package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.techullurgy.howzapp.feature.chat.database.entities.ChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao: BaseDao<ChatEntity> {
    @Transaction
    @Query("SELECT * FROM ChatEntity")
    fun getAllChats(): Flow<List<ChatRelation>>

    @Transaction
    @Query("SELECT * FROM ChatEntity WHERE chatId = :chatId")
    fun getChatById(chatId: String): Flow<ChatRelation?>
}