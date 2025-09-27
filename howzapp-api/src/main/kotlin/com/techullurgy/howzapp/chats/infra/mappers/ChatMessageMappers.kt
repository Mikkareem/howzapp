package com.techullurgy.howzapp.chats.infra.mappers

import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.chats.infra.database.entities.messages.*
import com.techullurgy.howzapp.chats.models.*

fun ChatMessageEntity.toDomain(): ChatMessage {
    return when(this) {
        is DocumentMessageEntity -> DocumentMessage(
            id = id,
            documentUrl = documentUrl,
            documentName = documentName
        )
        is AudioMessageEntity -> AudioMessage(
            id = id,
            audioUrl = mediaUrl!!
        )
        is ImageMessageEntity -> ImageMessage(
            id = id,
            imageUrl = mediaUrl!!
        )
        is VideoMessageEntity -> VideoMessage(
            id = id,
            videoUrl = mediaUrl!!
        )
        is StickerMessageEntity -> StickerMessage(
            id = id,
            stickerUrl = stickerUrl
        )
        is TextMessageEntity -> TextMessage(
            id = id,
            text = text
        )
        else -> TODO()
    }
}