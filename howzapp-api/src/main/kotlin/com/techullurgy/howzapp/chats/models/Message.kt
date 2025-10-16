package com.techullurgy.howzapp.chats.models

import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.id

sealed interface Message {
    val id: MessageId
}

data class TextMessage(
    override val id: MessageId = MessageId.id,
    val text: String,
) : Message

data class ImageMessage(
    override val id: MessageId = MessageId.id,
    val imageUrl: String
) : Message

data class AudioMessage(
    override val id: MessageId = MessageId.id,
    val audioUrl: String,
) : Message

data class VideoMessage(
    override val id: MessageId = MessageId.id,
    val videoUrl: String,
) : Message

data class DocumentMessage(
    override val id: MessageId = MessageId.id,
    val documentUrl: String,
    val documentName: String,
) : Message

data class StickerMessage(
    override val id: MessageId = MessageId.id,
    val stickerUrl: String,
) : Message

data class DeletedMessage(
    override val id: MessageId
) : Message