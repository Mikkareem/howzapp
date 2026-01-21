package com.techullurgy.howzapp.feature.auth.api.navigation

import androidx.compose.runtime.Composable

interface ILoginScreen {
    @Composable
    operator fun invoke(
        onLoginSuccess: () -> Unit,
        onForgotPasswordClick: () -> Unit,
        onCreateAccountClick: () -> Unit
    )
}

interface IRegisterScreen {
    @Composable
    operator fun invoke(
        onRegisterSuccess: (String) -> Unit,
        onLoginClick: () -> Unit
    )
}

