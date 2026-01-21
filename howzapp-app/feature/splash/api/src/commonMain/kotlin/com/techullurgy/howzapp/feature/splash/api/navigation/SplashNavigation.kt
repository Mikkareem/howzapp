package com.techullurgy.howzapp.feature.splash.api.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.techullurgy.howzapp.core.utils.inject
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

fun EntryProviderScope<Any>.splashRoute(
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
) {
    entry<SplashRoute> {
        inject<ISplashScreen>().invoke(
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}