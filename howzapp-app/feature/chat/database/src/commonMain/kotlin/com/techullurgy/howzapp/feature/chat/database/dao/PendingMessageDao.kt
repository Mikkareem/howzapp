package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.techullurgy.howzapp.feature.chat.database.entities.PendingMessageEntity
import com.techullurgy.howzapp.feature.chat.database.entities.PendingMessageRelation
import com.techullurgy.howzapp.feature.chat.database.models.SerializableUploadStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transform

@Dao
interface PendingMessageDao: BaseDao<PendingMessageEntity> {

    @Query("SELECT * FROM PendingMessageEntity WHERE pendingId = :pendingId")
    suspend fun getPendingMessageById(pendingId: String): PendingMessageRelation?

    @Query("DELETE FROM PendingMessageEntity WHERE pendingId = :pendingId")
    suspend fun deletePendingMessage(pendingId: String)

    @Transaction
    @Query("SELECT * FROM PendingMessageEntity WHERE isReady = true")
    fun getPendingMessagesThatAreReadyToSync(): Flow<List<PendingMessageRelation>>

    @Query("UPDATE PendingMessageEntity SET isReady = true WHERE pendingId = :pendingId")
    fun markPendingMessageAsReadyToSync(pendingId: String)

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