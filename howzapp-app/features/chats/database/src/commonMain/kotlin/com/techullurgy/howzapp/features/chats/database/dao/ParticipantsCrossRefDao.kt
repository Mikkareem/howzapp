package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.ChatParticipantCrossRef
import com.techullurgy.howzapp.core.database.BaseDao

@Dao
interface ParticipantsCrossRefDao: BaseDao<ChatParticipantCrossRef> {
    @Query("DELETE FROM ChatParticipantCrossRef")
    suspend fun deleteAll()
}