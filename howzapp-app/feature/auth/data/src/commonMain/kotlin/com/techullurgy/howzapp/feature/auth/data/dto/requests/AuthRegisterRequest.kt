package com.techullurgy.howzapp.feature.auth.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRegisterRequest(
    val email: String,
    val username: String,
    val password: String
)