package com.techullurgy.howzapp.features.chats.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.features.chats.database.entities.UploadablePendingMessageEntity
import com.techullurgy.howzapp.features.chats.database.models.SerializableUploadStatus
import com.techullurgy.howzapp.core.database.BaseDao

@Dao
interface UploadablePendingMessageDao: BaseDao<UploadablePendingMessageEntity> {
    @Query("UPDATE UploadablePendingMessageEntity SET uploadStatus = :status WHERE pendingId = :uploadId")
    suspend fun updateStatus(uploadId: String, status: SerializableUploadStatus)

    @Query("DELETE FROM UploadablePendingMessageEntity")
    suspend fun deleteAll()
}