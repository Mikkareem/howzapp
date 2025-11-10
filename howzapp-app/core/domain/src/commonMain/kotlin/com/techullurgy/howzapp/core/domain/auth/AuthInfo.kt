package com.techullurgy.howzapp.core.domain.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val id: String
)
