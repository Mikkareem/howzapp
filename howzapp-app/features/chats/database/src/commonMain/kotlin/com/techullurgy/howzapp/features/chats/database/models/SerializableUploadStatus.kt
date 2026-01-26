package com.techullurgy.howzapp.features.chats.database.models

import com.techullurgy.howzapp.features.chats.database.utils.UploadStatusConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
sealed interface SerializableUploadStatus {

    val timestamp: Long

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_SUCCESS)
    data class Success(
        val publicUrl: String,
        override val timestamp: Long = Clock.System.now().toEpochMilliseconds()
    ) :
        SerializableUploadStatus

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_PROGRESS)
    data class Progress(
        val progressPercentage: Double,
        override val timestamp: Long = Clock.System.now().toEpochMilliseconds()
    ) :
        SerializableUploadStatus

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_CANCELLED)
    data class Cancelled(override val timestamp: Long = Clock.System.now().toEpochMilliseconds()) :
        SerializableUploadStatus

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_FAILED)
    data class Failed(override val timestamp: Long = Clock.System.now().toEpochMilliseconds()) :
        SerializableUploadStatus

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_STARTED)
    data class Started(override val timestamp: Long = Clock.System.now().toEpochMilliseconds()) :
        SerializableUploadStatus

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_TRIGGERED)
    data class Triggered(override val timestamp: Long = Clock.System.now().toEpochMilliseconds()) :
        SerializableUploadStatus
}