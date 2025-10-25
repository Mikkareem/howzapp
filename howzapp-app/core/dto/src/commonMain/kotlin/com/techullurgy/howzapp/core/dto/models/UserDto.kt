package com.techullurgy.howzapp.core.dto.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: String,
    val name: String,
    val profilePictureUrl: String
)