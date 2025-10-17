package com.techullurgy.howzapp.chats.models

import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.users.models.AppUser

sealed interface ChatType {
    val id: ChatId
}

data class OneToOneChat(
    override val id: ChatId,
    val participant1: AppUser,
    val participant2: AppUser
) : ChatType

data class GroupChat(
    override val id: ChatId,
    val originator: AppUser,
    val participants: List<AppUser>,
    val title: String,
    val profilePictureUrl: String = ""
) : ChatType