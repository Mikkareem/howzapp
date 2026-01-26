package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.core.database.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantsDao : BaseDao<ChatParticipantEntity> {
    @Query("SELECT * FROM ChatParticipantEntity")
    fun observeAllParticipants(): Flow<List<ChatParticipantEntity>>

    @Query("DELETE FROM ChatParticipantEntity")
    suspend fun deleteAll()
}