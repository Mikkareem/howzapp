package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantsDao : BaseDao<ChatParticipantEntity> {
    @Query("SELECT * FROM ChatParticipantEntity")
    fun observeAllParticipants(): Flow<List<ChatParticipantEntity>>

    @Query("DELETE FROM ChatParticipantEntity")
    suspend fun deleteAll()
}