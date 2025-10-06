package com.techullurgy.howzapp.feature.chat.database.models

import com.techullurgy.howzapp.feature.chat.database.utils.UploadStatusConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SerializableUploadStatus {

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_SUCCESS)
    data class Success(val publicUrl: String): SerializableUploadStatus

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_PROGRESS)
    data class Progress(val progressPercentage: Double): SerializableUploadStatus

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_CANCELLED)
    data object Cancelled: SerializableUploadStatus

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_FAILED)
    data object Failed: SerializableUploadStatus

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_STARTED)
    data object Started: SerializableUploadStatus

    @Serializable
    @SerialName(UploadStatusConstants.DISCRIMINATOR_UPLOAD_TRIGGERED)
    data object Triggered: SerializableUploadStatus
}