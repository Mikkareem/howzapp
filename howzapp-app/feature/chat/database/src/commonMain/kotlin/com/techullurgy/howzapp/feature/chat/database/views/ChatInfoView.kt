package com.techullurgy.howzapp.feature.chat.database.views

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.database.models.SerializableChatType
import com.techullurgy.howzapp.feature.chat.database.utils.ChatTypeConstants
import com.techullurgy.howzapp.feature.chat.database.utils.DISCRIMINATOR_COLUMN
import kotlinx.serialization.Serializable

@Serializable
@DatabaseView(
    viewName = "view__chat_info_of_the_chats",
    value = """
        SELECT c.chatId,
                c.type AS chatType,
                cp2.userId AS originator_userId,
                cp2.onlineStatus AS originator_onlineStatus,
                cp2.username AS originator_username,
                cp2.profilePictureUrl AS originator_profilePictureUrl
        FROM ChatEntity c
        LEFT JOIN ChatParticipantEntity cp
            ON json_extract(c.type, '$.$DISCRIMINATOR_COLUMN') = '${ChatTypeConstants.DISCRIMINATOR_DIRECT_CHAT}'
            AND cp.userId = json_extract(c.type, '$.participantId')
        JOIN ChatParticipantEntity cp2 ON cp2.userId = c.originator
        JOIN ChatParticipantCrossRef cpcr2 ON cpcr2.chatId = c.chatId AND cpcr2.userId = cp2.userId
    """
)
data class ChatInfoView(
    val chatId: String,
    val chatType: SerializableChatType,
    @Embedded(prefix = "originator_") val originator: ChatParticipantEntity
)
