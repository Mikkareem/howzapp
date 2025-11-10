package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.feature.chat.database.entities.DirectChatEntity

@Dao
interface DirectChatDao: BaseDao<DirectChatEntity>{
    @Query("DELETE FROM DirectChatEntity")
    suspend fun deleteAll()
}