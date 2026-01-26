package com.techullurgy.howzapp.features.auth.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthEmailRequest(
    val email: String
)