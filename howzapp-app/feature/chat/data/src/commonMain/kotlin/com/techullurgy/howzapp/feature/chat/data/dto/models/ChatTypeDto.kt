package com.techullurgy.howzapp.feature.chat.data.dto.models

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface ChatTypeDto {
    val chatId: String
}

@Serializable
internal data class DirectChatDto(
    override val chatId: String,
    val participant1: UserDto,
    val participant2: UserDto
): ChatTypeDto

@Serializable
internal data class GroupChatDto(
    override val chatId: String,
    val originator: UserDto,
    val title: String,
    val participants: List<UserDto>,
    val profilePictureUrl: String? = null
): ChatTypeDto
