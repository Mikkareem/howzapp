package com.techullurgy.howzapp.feature.splash.api.navigation

import androidx.compose.runtime.Composable

interface ISplashScreen {
    @Composable
    operator fun invoke(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    )
}