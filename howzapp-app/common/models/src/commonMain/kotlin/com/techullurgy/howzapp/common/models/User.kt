package com.techullurgy.howzapp.common.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val name: String,
    val profilePictureUrl: String
)