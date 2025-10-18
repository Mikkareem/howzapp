package com.techullurgy.howzapp.feature.chat.domain.models

import kotlin.time.Clock

sealed interface UploadStatus {
    val timestamp: Long
    data class Success(val publicUrl: String, override val timestamp: Long = Clock.System.now().toEpochMilliseconds()): UploadStatus
    data class Failed(override val timestamp: Long = Clock.System.now().toEpochMilliseconds()): UploadStatus
    data class Started(override val timestamp: Long = Clock.System.now().toEpochMilliseconds()): UploadStatus
    data class Progress(val progressPercentage: Double, override val timestamp: Long = Clock.System.now().toEpochMilliseconds()): UploadStatus
    data class Triggered(override val timestamp: Long = Clock.System.now().toEpochMilliseconds()): UploadStatus
    data class Cancelled(override val timestamp: Long = Clock.System.now().toEpochMilliseconds()): UploadStatus
}