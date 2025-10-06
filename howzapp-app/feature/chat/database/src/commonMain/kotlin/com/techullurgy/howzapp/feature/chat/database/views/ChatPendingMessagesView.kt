package com.techullurgy.howzapp.feature.chat.database.views

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.techullurgy.howzapp.feature.chat.database.utils.DISCRIMINATOR_COLUMN
import com.techullurgy.howzapp.feature.chat.database.utils.MessageConstants
import kotlinx.serialization.Serializable

@Serializable
@DatabaseView(
    viewName = "view__pending_messages_on_all_the_chats",
    value = """
        SELECT * FROM view__full_message m
        WHERE
            json_extract(m.message, '$.$DISCRIMINATOR_COLUMN') IN (
                '${MessageConstants.DISCRIMINATOR_NON_UPLOADABLE_PENDING_MESSAGE}',
                '${MessageConstants.DISCRIMINATOR_UPLOADABLE_PENDING_MESSAGE}'
            )
            AND json_extract(m.message, '$.isReadyToSync') = 'true'
            AND m.status_status = 'PENDING'
    """
)
data class ChatPendingMessagesView(
    @Embedded val message: ChatMessageView
)