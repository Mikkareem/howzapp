package com.techullurgy.howzapp.feature.chat.domain.models

sealed interface Message {

    sealed interface UploadableMessage: Message
    sealed interface NonUploadableMessage: Message

    sealed interface PendingMessage: Message {
        val isReadyToSync: Boolean
        val originalMessage: Message
    }

    data class UploadablePendingMessage(
        val uploadId: String,
        val status: UploadStatus,
        override val originalMessage: UploadableMessage,
        override val isReadyToSync: Boolean = false
    ): PendingMessage

    data class NonUploadablePendingMessage(
        override val originalMessage: NonUploadableMessage,
        override val isReadyToSync: Boolean = true
    ): PendingMessage

    data class TextMessage(
        val text: String
    ): NonUploadableMessage

    data class ImageMessage(
        val imageUrl: String,
        val optionalText: String? = null
    ): UploadableMessage

    data class VideoMessage(
        val videoUrl: String,
        val optionalText: String? = null
    ): UploadableMessage

    data class AudioMessage(
        val audioUrl: String,
        val optionalText: String? = null
    ): UploadableMessage

    data class DocumentMessage(
        val documentName: String,
        val documentUrl: String,
        val optionalText: String? = null
    ): UploadableMessage
}