package com.techullurgy.howzapp.features.auth.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRefreshRequest(
    val refreshToken: String
)
