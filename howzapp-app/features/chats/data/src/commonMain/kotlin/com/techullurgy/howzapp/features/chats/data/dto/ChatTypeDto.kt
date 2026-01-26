package com.techullurgy.howzapp.features.chats.data.dto

import com.techullurgy.howzapp.common.dto.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ChatTypeDto {
    val chatId: String
}

@Serializable
@SerialName("direct_chat")
data class DirectChatDto(
    override val chatId: String,
    val participant1: UserDto,
    val participant2: UserDto
): ChatTypeDto

@Serializable
@SerialName("group_chat")
data class GroupChatDto(
    override val chatId: String,
    val originator: UserDto,
    val title: String,
    val participants: List<UserDto>,
    val profilePictureUrl: String? = null
): ChatTypeDto
