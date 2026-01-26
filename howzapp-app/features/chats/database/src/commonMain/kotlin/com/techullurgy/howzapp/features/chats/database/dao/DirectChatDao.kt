package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.DirectChatEntity
import com.techullurgy.howzapp.core.database.BaseDao

@Dao
interface DirectChatDao : BaseDao<DirectChatEntity> {
    @Query("DELETE FROM DirectChatEntity")
    suspend fun deleteAll()
}