package com.techullurgy.howzapp.feature.chat.data.dto.models

import kotlinx.serialization.Serializable

@Serializable
internal data class UserDto(
    val userId: String,
    val name: String,
    val profilePictureUrl: String
)