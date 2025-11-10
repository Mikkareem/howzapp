package com.techullurgy.howzapp.auth.dto

data class LoginRequest(
    val email: String,
    val password: String
)