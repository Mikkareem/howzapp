package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.feature.chat.database.entities.OnlineUsersEntity

@Dao
interface OnlineUsersDao: BaseDao<OnlineUsersEntity> {
    @Query("DELETE FROM OnlineUsersEntity")
    suspend fun deleteAll()
}