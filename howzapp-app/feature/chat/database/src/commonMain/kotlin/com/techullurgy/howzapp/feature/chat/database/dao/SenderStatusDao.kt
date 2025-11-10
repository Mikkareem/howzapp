package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.feature.chat.database.entities.SenderStatusEntity

@Dao
interface SenderStatusDao: BaseDao<SenderStatusEntity> {
    @Query("DELETE FROM SenderStatusEntity")
    suspend fun deleteAll()
}