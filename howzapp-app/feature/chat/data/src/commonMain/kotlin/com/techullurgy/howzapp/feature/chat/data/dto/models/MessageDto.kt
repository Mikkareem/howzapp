package com.techullurgy.howzapp.feature.chat.data.dto.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface MessageDto

@Serializable
@SerialName("text_message")
internal data class TextMessageDto(val text: String) : MessageDto

@Serializable
@SerialName("image_message")
internal data class ImageMessageDto(val imageUrl: String) : MessageDto

@Serializable
@SerialName("video_message")
internal data class VideoMessageDto(val videoUrl: String) : MessageDto

@Serializable
@SerialName("audio_message")
internal data class AudioMessageDto(val audioUrl: String) : MessageDto

@Serializable
@SerialName("document_message")
internal data class DocumentMessageDto(val documentUrl: String, val documentName: String) : MessageDto

@Serializable
@SerialName("contact_message")
internal data class ContactMessageDto(val contactName: String, val contactId: String) : MessageDto