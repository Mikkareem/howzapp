package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.feature.chat.database.entities.ReceiverStatusEntity

@Dao
interface ReceiverStatusDao: BaseDao<ReceiverStatusEntity> {
    @Query("DELETE FROM ReceiverStatusEntity")
    suspend fun deleteAll()
}