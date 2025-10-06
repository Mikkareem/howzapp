package com.techullurgy.howzapp.core.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)