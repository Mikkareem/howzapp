package com.techullurgy.howzapp.feature.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val id: String
)