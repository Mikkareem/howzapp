package com.techullurgy.howzapp.feature.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import com.techullurgy.howzapp.core.presentation.util.UiText

internal data class LoginState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val passwordTextFieldState: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val isLoggingIn: Boolean = false,
    val error: UiText? = null
)