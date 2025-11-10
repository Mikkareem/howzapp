package com.techullurgy.howzapp.core.domain.networking

import kotlinx.serialization.Serializable

@Serializable
sealed interface NetworkUploadStatus {
    @Serializable
    data object Failed: NetworkUploadStatus
    @Serializable
    data object Cancelled: NetworkUploadStatus
    @Serializable
    data object Started: NetworkUploadStatus
    @Serializable
    data class Progress(val progress: Double): NetworkUploadStatus
    @Serializable
    data class Success(val url: String): NetworkUploadStatus
}