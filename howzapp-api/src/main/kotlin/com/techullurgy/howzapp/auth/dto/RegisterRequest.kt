package com.techullurgy.howzapp.auth.dto

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)