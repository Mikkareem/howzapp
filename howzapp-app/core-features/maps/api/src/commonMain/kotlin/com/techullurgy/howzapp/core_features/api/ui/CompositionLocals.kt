package com.techullurgy.howzapp.core_features.api.ui

import androidx.compose.runtime.staticCompositionLocalOf

val LocalGoogleMapsApiKey = staticCompositionLocalOf<String> {
    error("Google Maps API key not provided")
}