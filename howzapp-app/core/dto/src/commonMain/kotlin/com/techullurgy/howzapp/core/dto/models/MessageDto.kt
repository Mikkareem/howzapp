package com.techullurgy.howzapp.core.dto.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MessageDto

@Serializable
@SerialName("text_message")
data class TextMessageDto(val text: String) : MessageDto

@Serializable
@SerialName("image_message")
data class ImageMessageDto(val imageUrl: String) : MessageDto

@Serializable
@SerialName("video_message")
data class VideoMessageDto(val videoUrl: String) : MessageDto

@Serializable
@SerialName("audio_message")
data class AudioMessageDto(val audioUrl: String) : MessageDto

@Serializable
@SerialName("document_message")
data class DocumentMessageDto(val documentUrl: String, val documentName: String) : MessageDto

@Serializable
@SerialName("contact_message")
data class ContactMessageDto(val contactName: String, val contactId: String) : MessageDto

@Serializable
@SerialName("location_message")
data class LocationMessageDto(val latitude: Double, val longitude: Double) : MessageDto