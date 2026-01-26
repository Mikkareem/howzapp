package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.MessageEntity
import com.techullurgy.howzapp.core.database.BaseDao

@Dao
interface MessageDao : BaseDao<MessageEntity> {
    @Query("DELETE FROM MessageEntity")
    suspend fun deleteAll()
}