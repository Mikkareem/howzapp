package com.techullurgy.howzapp.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: String,
    val name: String,
    val profilePictureUrl: String
)