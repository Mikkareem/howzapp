package com.techullurgy.howzapp.features.auth.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)