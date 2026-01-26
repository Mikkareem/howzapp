package com.techullurgy.howzapp.core.session

data class SessionInfo(
    val id: String,
    val accessToken: String,
    val refreshToken: String
)