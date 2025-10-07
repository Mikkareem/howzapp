package com.techullurgy.howzapp.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val background: Color,
    val container1: Color,
    val container2: Color,
    val container3: Color,
    val content: Color,
    val content1: Color,
    val content2: Color,
    val content3: Color
)

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No app colors provided")
}

val LightAppColors = AppColors(
    background = Color(0xFFE3E3E3),
    container1 = Color(0xFFDD91E4),
    container2 = Color(0xFF773E7C),
    container3 = Color(0xFFC861D2),
    content = Color(0xFF000000),
    content1 = Color(0xFF000000),
    content2 = Color(0xFFFFFFFF),
    content3 = Color(0xFF000000),
)

val DarkAppColors = AppColors(
    background = Color(0xFF1A1A1A),
    container1 = Color(0xFFC861D2),
    container2 = Color(0xFF773E7C),
    container3 = Color(0xFFDD91E4),
    content = Color(0xFFFFFFFF),
    content1 = Color(0xFF000000),
    content2 = Color(0xFFFFFFFF),
    content3 = Color(0xFF000000),
)