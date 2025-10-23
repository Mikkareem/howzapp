package com.techullurgy.howzapp.feature.auth.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthResetPasswordRequest(
    val newPassword: String,
    val token: String
)