package com.techullurgy.howzapp.feature.auth.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRefreshRequest(
    val refreshToken: String
)
