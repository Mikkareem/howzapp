package com.techullurgy.howzapp.core.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthResetPasswordRequest(
    val newPassword: String,
    val token: String
)