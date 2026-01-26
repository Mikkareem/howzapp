package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.techullurgy.howzapp.features.chats.database.entities.ChatEntity
import com.techullurgy.howzapp.features.chats.database.entities.ChatRelation
import com.techullurgy.howzapp.core.database.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao: BaseDao<ChatEntity> {
    @Transaction
    @Query("SELECT * FROM ChatEntity")
    fun getAllChats(): Flow<List<ChatRelation>>

    @Transaction
    @Query("SELECT * FROM ChatEntity WHERE chatId = :chatId")
    fun getChatById(chatId: String): Flow<ChatRelation?>

    @Query("DELETE FROM ChatEntity")
    suspend fun deleteAll()
}