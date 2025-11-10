package com.techullurgy.howzapp.core.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginRequest(
    val email: String,
    val password: String
)