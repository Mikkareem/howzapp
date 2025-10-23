package com.techullurgy.howzapp.feature.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    val accessToken: String,
    val refreshToken: String,
    val user: UserSerializable
)