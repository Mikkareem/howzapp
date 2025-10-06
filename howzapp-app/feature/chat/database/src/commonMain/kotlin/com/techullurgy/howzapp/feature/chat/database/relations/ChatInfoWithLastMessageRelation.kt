package com.techullurgy.howzapp.feature.chat.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.techullurgy.howzapp.feature.chat.database.views.ChatInfoView
import com.techullurgy.howzapp.feature.chat.database.views.ChatLastMessageView
import kotlinx.serialization.Serializable

@Serializable
data class ChatInfoWithLastMessageRelation(
    @Embedded
    val chatInfo: ChatInfoView,

    @Relation(
        entity = ChatLastMessageView::class,
        parentColumn = "chatId",
        entityColumn = "chatId"
    )
    val lastMessage: ChatLastMessageView
)
