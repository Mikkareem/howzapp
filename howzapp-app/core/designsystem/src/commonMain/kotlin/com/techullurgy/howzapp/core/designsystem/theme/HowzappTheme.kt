package com.techullurgy.howzapp.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun HowzAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appColors = if(darkTheme) DarkAppColors else LightAppColors
    val colorScheme = if(darkTheme) DarkColorScheme else LightColorScheme
    val extendedScheme = if(darkTheme) DarkExtendedColors else LightExtendedColors

    val contentColor = if (darkTheme) Color.White else Color.Black

    CompositionLocalProvider(
        LocalAppColors provides appColors,
        LocalExtendedColors provides extendedScheme,
        LocalContentColor provides contentColor
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}