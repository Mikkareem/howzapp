package com.techullurgy.howzapp.feature.splash.api.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.techullurgy.howzapp.feature.splash.presentation.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

fun EntryProviderScope<Any>.splashRoute(
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
) {
    entry<SplashRoute> {
        SplashScreen(
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}