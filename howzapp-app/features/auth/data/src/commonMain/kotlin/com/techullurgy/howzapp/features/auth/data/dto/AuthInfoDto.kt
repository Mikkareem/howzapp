package com.techullurgy.howzapp.features.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoDto(
    val accessToken: String,
    val refreshToken: String,
    val id: String
)
