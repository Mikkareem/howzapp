package com.techullurgy.howzapp.feature.chat.database.views

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.techullurgy.howzapp.feature.chat.database.utils.DISCRIMINATOR_COLUMN
import com.techullurgy.howzapp.feature.chat.database.utils.MessageConstants
import com.techullurgy.howzapp.feature.chat.database.utils.UploadStatusConstants
import kotlinx.serialization.Serializable

@Serializable
@DatabaseView(
    viewName = "view__pending_uploads_on_chats_that_are_complete_successfully",
    value = """
        SELECT * FROM view__full_message m
        WHERE json_extract(m.message, '$.$DISCRIMINATOR_COLUMN') = '${MessageConstants.DISCRIMINATOR_UPLOADABLE_PENDING_MESSAGE}'
            AND json_extract(m.message, '$.uploadStatus.$DISCRIMINATOR_COLUMN') = '${UploadStatusConstants.DISCRIMINATOR_UPLOAD_SUCCESS}'
    """
)
data class ChatPendingUploadsSuccessView(
    @Embedded val uploads: ChatMessageView
)