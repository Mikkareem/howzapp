package com.techullurgy.howzapp.feature.chat.domain.models

sealed interface Message

sealed interface PendingMessage: Message {
    val originalMessage: OriginalMessage

    data class UploadablePendingMessage(
        val uploadId: String,
        val status: UploadStatus,
        override val originalMessage: OriginalMessage,
    ): PendingMessage

    data class NonUploadablePendingMessage(
        override val originalMessage: OriginalMessage,
    ): PendingMessage
}

sealed interface OriginalMessage: Message {
    data class TextMessage(
        val text: String
    ): OriginalMessage

    data class ImageMessage(
        val imageUrl: String,
        val optionalText: String? = null
    ): OriginalMessage

    data class VideoMessage(
        val videoUrl: String,
        val optionalText: String? = null
    ): OriginalMessage

    data class AudioMessage(
        val audioUrl: String,
        val optionalText: String? = null
    ): OriginalMessage

    data class DocumentMessage(
        val documentName: String,
        val documentUrl: String,
        val optionalText: String? = null
    ): OriginalMessage
}