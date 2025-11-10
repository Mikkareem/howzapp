package com.techullurgy.howzapp.feature.auth.presentation.login

internal sealed interface LoginEvent {
    data object Success: LoginEvent
}