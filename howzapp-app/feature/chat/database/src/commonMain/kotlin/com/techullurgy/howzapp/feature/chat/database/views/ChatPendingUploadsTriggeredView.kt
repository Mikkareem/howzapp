package com.techullurgy.howzapp.feature.chat.database.views

import androidx.room.DatabaseView
import androidx.room.Embedded
import kotlinx.serialization.Serializable

@Serializable
@DatabaseView(
    viewName = "view__pending_uploads_on_chats_that_are_triggered",
    value = """
        SELECT * FROM view__full_message m
        WHERE json_extract(m.message, '$.type') = 'SerializableMessage.UploadablePendingMessage'
            AND json_extract(m.message, '$.uploadStatus') = 'SerializableUploadStatus.Triggered'
    """
)
data class ChatPendingUploadsTriggeredView(
    @Embedded val uploads: ChatMessageView
)