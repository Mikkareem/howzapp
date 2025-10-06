package com.techullurgy.howzapp.feature.chat.database.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantCrossRef
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.database.views.ChatInfoView
import com.techullurgy.howzapp.feature.chat.database.views.ChatMessageView
import kotlinx.serialization.Serializable

@Serializable
data class ChatDetails(
    @Embedded val chatInfo: ChatInfoView,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "userId",
        associateBy = Junction(ChatParticipantCrossRef::class)
    )
    val participants: List<ChatParticipantEntity>,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId",
    )
    val messages: List<ChatMessageView>
)
