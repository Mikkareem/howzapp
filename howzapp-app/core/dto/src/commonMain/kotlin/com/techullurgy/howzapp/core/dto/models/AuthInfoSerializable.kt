package com.techullurgy.howzapp.core.dto.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    val accessToken: String,
    val refreshToken: String,
    val id: String
)