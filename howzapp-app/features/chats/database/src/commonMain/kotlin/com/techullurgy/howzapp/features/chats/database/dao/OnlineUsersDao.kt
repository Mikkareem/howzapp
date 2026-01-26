package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.OnlineUsersEntity
import com.techullurgy.howzapp.core.database.BaseDao

@Dao
interface OnlineUsersDao : BaseDao<OnlineUsersEntity> {
    @Query("DELETE FROM OnlineUsersEntity")
    suspend fun deleteAll()
}