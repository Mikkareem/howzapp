package com.techullurgy.howzapp.chats.models

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.users.models.AppUser

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(DirectChat ::class, name = "direct_chat"),
    JsonSubTypes.Type(GroupChat ::class, name = "group_chat"),
)
sealed interface ChatType {
    val chatId: ChatId
}

data class DirectChat(
    override val chatId: ChatId,
    val participant1: AppUser,
    val participant2: AppUser
) : ChatType

data class GroupChat(
    override val chatId: ChatId,
    val originator: AppUser,
    val participants: List<AppUser>,
    val title: String,
    val profilePictureUrl: String = ""
) : ChatType