package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.feature.chat.database.entities.PendingReceiptsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingReceiptsDao: BaseDao<PendingReceiptsEntity> {
    @Query("SELECT * FROM PendingReceiptsEntity WHERE isPending = true")
    fun observePendingReceipts(): Flow<List<PendingReceiptsEntity>>

    @Query("UPDATE PendingReceiptsEntity SET isPending = false WHERE id = :id")
    suspend fun updateReceiptAsComplete(id: Long)
}