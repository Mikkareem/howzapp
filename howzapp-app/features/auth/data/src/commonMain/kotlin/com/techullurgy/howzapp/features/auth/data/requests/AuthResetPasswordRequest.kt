package com.techullurgy.howzapp.features.auth.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthResetPasswordRequest(
    val newPassword: String,
    val token: String
)