package com.techullurgy.howzapp.feature.chat.database.views

import androidx.room.DatabaseView
import com.techullurgy.howzapp.feature.chat.database.utils.DISCRIMINATOR_COLUMN
import com.techullurgy.howzapp.feature.chat.database.utils.MessageConstants
import com.techullurgy.howzapp.feature.chat.database.utils.UploadStatusConstants

@DatabaseView(
    viewName = "view__pending_uploads_cancelled_on_chats",
    value = """
        SELECT m.messageId
        FROM ChatMessageEntity m
        WHERE json_extract(m.message, '$.$DISCRIMINATOR_COLUMN') = '${MessageConstants.DISCRIMINATOR_UPLOADABLE_PENDING_MESSAGE}'
            AND json_extract(m.message, '$.uploadStatus.$DISCRIMINATOR_COLUMN') = '${UploadStatusConstants.DISCRIMINATOR_UPLOAD_CANCELLED}'
    """
)
data class ChatPendingUploadsCancelledView(
    val messageId: String
)