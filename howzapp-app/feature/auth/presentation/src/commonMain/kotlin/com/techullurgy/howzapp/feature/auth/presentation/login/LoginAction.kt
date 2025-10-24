package com.techullurgy.howzapp.feature.auth.presentation.login

internal sealed interface LoginAction {
    data object OnTogglePasswordVisibility: LoginAction
    data object OnForgotPasswordClick: LoginAction
    data object OnLoginClick: LoginAction
    data object OnSignUpClick: LoginAction
}