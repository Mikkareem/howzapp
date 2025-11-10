package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.feature.chat.database.entities.MessageEntity

@Dao
interface MessageDao: BaseDao<MessageEntity> {
    @Query("DELETE FROM MessageEntity")
    suspend fun deleteAll()
}