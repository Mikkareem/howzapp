package com.techullurgy.howzapp.features.auth.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginRequest(
    val email: String,
    val password: String
)