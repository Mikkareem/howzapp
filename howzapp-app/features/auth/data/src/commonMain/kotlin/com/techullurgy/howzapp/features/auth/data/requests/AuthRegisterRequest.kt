package com.techullurgy.howzapp.features.auth.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRegisterRequest(
    val email: String,
    val username: String,
    val password: String
)