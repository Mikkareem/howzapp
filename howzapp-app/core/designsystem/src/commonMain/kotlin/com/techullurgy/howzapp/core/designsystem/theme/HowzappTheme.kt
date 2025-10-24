package com.techullurgy.howzapp.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun HowzAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appColors = if(darkTheme) DarkAppColors else LightAppColors
    val colorScheme = if(darkTheme) DarkColorScheme else LightColorScheme
    val extendedScheme = if(darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(
        LocalAppColors provides appColors,
        LocalExtendedColors provides extendedScheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}