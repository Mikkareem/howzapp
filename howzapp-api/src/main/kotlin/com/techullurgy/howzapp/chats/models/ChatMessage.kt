package com.techullurgy.howzapp.chats.models

import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.id

sealed interface ChatMessage

data class TextMessage(
    val id: MessageId = MessageId.id,
    val text: String,
): ChatMessage

data class ImageMessage(
    val id: MessageId = MessageId.id,
    val imageUrl: String
): ChatMessage

data class AudioMessage(
    val id: MessageId = MessageId.id,
    val audioUrl: String,
): ChatMessage

data class VideoMessage(
    val id: MessageId = MessageId.id,
    val videoUrl: String,
): ChatMessage

data class DocumentMessage(
    val id: MessageId = MessageId.id,
    val documentUrl: String,
    val documentName: String,
): ChatMessage

data class StickerMessage(
    val id: MessageId = MessageId.id,
    val stickerUrl: String,
): ChatMessage