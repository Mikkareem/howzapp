package com.techullurgy.howzapp.core.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRefreshRequest(
    val refreshToken: String
)
