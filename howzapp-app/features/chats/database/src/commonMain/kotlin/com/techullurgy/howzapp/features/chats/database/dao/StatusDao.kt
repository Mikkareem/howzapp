package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.StatusEntity
import com.techullurgy.howzapp.core.database.BaseDao

@Dao
interface StatusDao : BaseDao<StatusEntity> {
    @Query("DELETE FROM StatusEntity")
    suspend fun deleteAll()
}