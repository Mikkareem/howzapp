package com.techullurgy.howzapp.core.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthResetPasswordRequest(
    val newPassword: String,
    val token: String
)