package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.GroupChatEntity
import com.techullurgy.howzapp.core.database.BaseDao

@Dao
interface GroupChatDao : BaseDao<GroupChatEntity> {
    @Query("DELETE FROM GroupChatEntity")
    suspend fun deleteAll()
}