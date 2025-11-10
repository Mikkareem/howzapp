package com.techullurgy.howzapp.users.models

import com.techullurgy.howzapp.common.types.UserId

data class AuthenticatedUser(
    val id: UserId,
    val accessToken: String,
    val refreshToken: String,
)