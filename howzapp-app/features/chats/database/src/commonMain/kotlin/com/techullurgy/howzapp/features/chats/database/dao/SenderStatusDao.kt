package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.SenderStatusEntity
import com.techullurgy.howzapp.core.database.BaseDao

@Dao
interface SenderStatusDao : BaseDao<SenderStatusEntity> {
    @Query("DELETE FROM SenderStatusEntity")
    suspend fun deleteAll()
}