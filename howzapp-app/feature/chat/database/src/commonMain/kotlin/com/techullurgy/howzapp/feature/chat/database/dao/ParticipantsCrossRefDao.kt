package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantCrossRef

@Dao
interface ParticipantsCrossRefDao: BaseDao<ChatParticipantCrossRef> {
    @Query("DELETE FROM ChatParticipantCrossRef")
    suspend fun deleteAll()
}