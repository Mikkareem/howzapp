package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.ReceiverStatusEntity
import com.techullurgy.howzapp.core.database.BaseDao

@Dao
interface ReceiverStatusDao : BaseDao<ReceiverStatusEntity> {
    @Query("DELETE FROM ReceiverStatusEntity")
    suspend fun deleteAll()
}