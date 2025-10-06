package com.techullurgy.howzapp.feature.chat.domain.models

sealed interface UploadStatus {
    data class Success(val publicUrl: String): UploadStatus
    data object Failed: UploadStatus
    data object Started: UploadStatus
    data class Progress(val progressPercentage: Double): UploadStatus
    data object Triggered: UploadStatus
    data object Cancelled: UploadStatus
}