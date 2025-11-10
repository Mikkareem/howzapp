package com.techullurgy.howzapp.feature.auth.presentation.register

internal sealed interface RegisterEvent {
    data class Success(val email: String): RegisterEvent
}