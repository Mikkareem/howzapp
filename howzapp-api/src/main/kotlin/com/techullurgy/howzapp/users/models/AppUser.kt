package com.techullurgy.howzapp.users.models

import com.techullurgy.howzapp.common.types.UserId

data class AppUser(
    val id: UserId,
    val name: String,
    val profilePictureUrl: String
)