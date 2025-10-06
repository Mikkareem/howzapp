package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.entities.ChatEntity
import com.techullurgy.howzapp.feature.chat.database.relations.ChatInfoWithLastMessageRelation
import com.techullurgy.howzapp.feature.chat.database.views.ChatInfoView
import com.techullurgy.howzapp.feature.chat.domain.models.ChatInfo
import com.techullurgy.howzapp.feature.chat.domain.models.ChatInfoWithLastMessage

fun ChatInfoView.toDomain() = ChatInfo(
    chatType = chatType.toDomain(),
    chatTitle = chatTitle,
    chatId = chatId,
    chatProfilePicture = pictureUrl,
    originator = originator.toDomain()
)

fun ChatInfoWithLastMessageRelation.toDomain() = ChatInfoWithLastMessage(
    info = chatInfo.toDomain(),
    lastMessage = this.lastMessage.toDomain(),
    unreadCount = lastMessage.unreadMessagesCount
)

fun ChatInfo.toEntity() = ChatEntity(
    chatId = chatId,
    type = chatType.toSerializable(),
    originator = originator.userId
)