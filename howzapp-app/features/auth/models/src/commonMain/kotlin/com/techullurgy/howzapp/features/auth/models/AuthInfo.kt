package com.techullurgy.howzapp.features.auth.models

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val id: String
)