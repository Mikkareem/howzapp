package com.techullurgy.howzapp.features.chats.models

sealed interface Message

sealed interface PendingMessage: Message {
    val originalMessage: OriginalMessage

    data class UploadablePendingMessage(
        val status: UploadStatus,
        override val originalMessage: OriginalMessage.UploadableMessage,
    ): PendingMessage

    data class NonUploadablePendingMessage(
        override val originalMessage: OriginalMessage.NonUploadableMessage,
    ): PendingMessage
}

sealed interface OriginalMessage: Message {

    sealed interface UploadableMessage: OriginalMessage
    sealed interface NonUploadableMessage: OriginalMessage

    data class TextMessage(
        val text: String
    ): NonUploadableMessage

    data class LocationMessage(
        val latitude: Double,
        val longitude: Double
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