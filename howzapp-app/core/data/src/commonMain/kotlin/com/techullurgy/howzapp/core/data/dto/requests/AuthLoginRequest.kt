package com.techullurgy.howzapp.core.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginRequest(
    val email: String,
    val password: String
)