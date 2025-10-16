package com.techullurgy.howzapp.chats.models

import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.users.models.AppUser

sealed interface ChatType {
    val id: ChatId
    val originator: AppUser
}

data class OneToOneChat(
    override val id: ChatId,
    override val originator: AppUser,
    val participant: AppUser
) : ChatType

data class GroupChat(
    override val id: ChatId,
    override val originator: AppUser,
    val participants: List<AppUser>,
    val title: String,
    val profilePictureUrl: String = ""
) : ChatType