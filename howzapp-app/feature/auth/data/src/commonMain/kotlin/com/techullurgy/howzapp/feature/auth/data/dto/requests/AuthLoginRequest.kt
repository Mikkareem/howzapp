package com.techullurgy.howzapp.feature.auth.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginRequest(
    val email: String,
    val password: String
)