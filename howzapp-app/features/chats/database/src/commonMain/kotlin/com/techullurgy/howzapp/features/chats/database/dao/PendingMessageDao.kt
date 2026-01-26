package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.techullurgy.howzapp.features.chats.database.entities.PendingMessageEntity
import com.techullurgy.howzapp.features.chats.database.entities.PendingMessageRelation
import com.techullurgy.howzapp.features.chats.database.models.SerializableUploadStatus
import com.techullurgy.howzapp.core.database.BaseDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transform

@Dao
interface PendingMessageDao: BaseDao<PendingMessageEntity> {

    @Transaction
    @Query("SELECT * FROM PendingMessageEntity WHERE pendingId = :pendingId")
    suspend fun getPendingMessageById(pendingId: String): PendingMessageRelation?

    @Query("DELETE FROM PendingMessageEntity WHERE pendingId = :pendingId")
    suspend fun deletePendingMessage(pendingId: String)

    @Transaction
    @Query("SELECT * FROM PendingMessageEntity WHERE isReady = true")
    fun getPendingMessagesThatAreReadyToSync(): Flow<List<PendingMessageRelation>>

    @Query("UPDATE PendingMessageEntity SET isReady = true WHERE pendingId = :pendingId")
    suspend fun markPendingMessageAsReadyToSync(pendingId: String)

    @Query("DELETE FROM PendingMessageEntity")
    suspend fun deleteAll()

    @Transaction
    @Query("""
        SELECT p.*
        FROM PendingMessageEntity p
        JOIN UploadablePendingMessageEntity up
            ON up.pendingId = p.pendingId
        WHERE p.isReady = false
    """)
    fun getUploadablePendingMessagesThatAreUnready(): Flow<List<PendingMessageRelation>>

    fun getUploadablePendingMessagesThatAreTriggered(): Flow<List<PendingMessageRelation>> {
        return getUploadablePendingMessagesThatAreUnready()
            .distinctUntilChanged()
            .transform { list ->
                if(list.isNotEmpty()) {
                    val newList = list.filter { it.uploadable!!.uploadStatus is SerializableUploadStatus.Triggered }
                    emit(newList)
                }
            }
    }

    fun getUploadablePendingMessagesThatAreCancelled(): Flow<List<PendingMessageRelation>> {
        return getUploadablePendingMessagesThatAreUnready()
            .distinctUntilChanged()
            .transform { list ->
                if(list.isNotEmpty()) {
                    val newList = list.filter { it.uploadable!!.uploadStatus is SerializableUploadStatus.Cancelled }
                    emit(newList)
                }
            }
    }
}