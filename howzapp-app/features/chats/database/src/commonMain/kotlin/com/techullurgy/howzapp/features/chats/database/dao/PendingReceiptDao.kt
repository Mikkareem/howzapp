package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.PendingReceiptsEntity
import com.techullurgy.howzapp.core.database.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingReceiptsDao: BaseDao<PendingReceiptsEntity> {
    @Query("SELECT * FROM PendingReceiptsEntity WHERE isPending = true")
    fun observePendingReceipts(): Flow<List<PendingReceiptsEntity>>

    @Query("UPDATE PendingReceiptsEntity SET isPending = false WHERE id = :id")
    suspend fun updateReceiptAsComplete(id: Long)

    @Query("DELETE FROM PendingReceiptsEntity")
    suspend fun deleteAll()
}