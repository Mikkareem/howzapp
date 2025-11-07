package com.techullurgy.howzapp.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val ColorScheme.extended: ExtendedColors
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColors.current

@Immutable
data class ExtendedColors(
    // Button states
    val primaryHover: Color,
    val destructiveHover: Color,
    val destructiveSecondaryOutline: Color,
    val disabledOutline: Color,
    val disabledFill: Color,
    val successOutline: Color,
    val success: Color,
    val onSuccess: Color,
    val secondaryFill: Color,

    // Text variants
    val textPrimary: Color,
    val textTertiary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Surface variants
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val overlay: Color,

    // Accent colors
    val accentBlue: Color,
    val accentPurple: Color,
    val accentViolet: Color,
    val accentPink: Color,
    val accentOrange: Color,
    val accentYellow: Color,
    val accentGreen: Color,
    val accentRed: Color,
    val accentTeal: Color,
    val accentLightBlue: Color,
    val accentGrey: Color,

    // Cake colors for chat bubbles
    val cakeViolet: Color,
    val cakeGreen: Color,
    val cakeBlue: Color,
    val cakePink: Color,
    val cakeOrange: Color,
    val cakeYellow: Color,
    val cakeTeal: Color,
    val cakePurple: Color,
    val cakeRed: Color,
    val cakeMint: Color,
)

val LightExtendedColors = ExtendedColors(
    primaryHover = HowzappBrand600,
    destructiveHover = HowzappRed600,
    destructiveSecondaryOutline = HowzappRed200,
    disabledOutline = HowzappBase200,
    disabledFill = HowzappBase150,
    successOutline = HowzappBrand100,
    success = HowzappBrand600,
    onSuccess = HowzappBase0,
    secondaryFill = HowzappBase100,

    textPrimary = HowzappBase1000,
    textTertiary = HowzappBase800,
    textSecondary = HowzappBase900,
    textPlaceholder = HowzappNeutral2Light,
    textDisabled = HowzappBase400,

    surfaceLower = HowzappBase100,
    surfaceHigher = HowzappBase100,
    surfaceOutline = HowzappBase1000Alpha14,
    overlay = HowzappBase1000Alpha80,

    accentBlue = HowzappBlue,
    accentPurple = HowzappPurple,
    accentViolet = HowzappViolet,
    accentPink = HowzappPink,
    accentOrange = HowzappOrangeLight,
    accentYellow = HowzappYellow,
    accentGreen = HowzappAccentGreen,
    accentRed = HowzappAccentRed,
    accentTeal = HowzappTeal,
    accentLightBlue = HowzappLightBlue,
    accentGrey = HowzappGrey,

    cakeViolet = HowzappCakeLightViolet,
    cakeGreen = HowzappCakeLightGreen,
    cakeBlue = HowzappSkyBlueLight,
    cakePink = HowzappCakeLightPink,
    cakeOrange = HowzappCakeLightOrange,
    cakeYellow = HowzappCakeLightYellow,
    cakeTeal = HowzappCakeLightTeal,
    cakePurple = HowzappCakeLightPurple,
    cakeRed = HowzappCakeLightRed,
    cakeMint = HowzappCakeLightMint,
)

val DarkExtendedColors = ExtendedColors(
    primaryHover = HowzappBrand600,
    destructiveHover = HowzappRed600,
    destructiveSecondaryOutline = HowzappRed200,
    disabledOutline = HowzappBase900,
    disabledFill = HowzappBase1000,
    successOutline = HowzappBrand500Alpha40,
    success = HowzappBrand500,
    onSuccess = HowzappBase1000,
    secondaryFill = HowzappBase900,

    textPrimary = HowzappBase0,
    textTertiary = HowzappBase200,
    textSecondary = HowzappBase150,
    textPlaceholder = HowzappNeutral2Dark,
    textDisabled = HowzappBase500,

    surfaceLower = HowzappBase1000,
    surfaceHigher = HowzappBase900,
    surfaceOutline = HowzappBase100Alpha10Alt,
    overlay = HowzappBase1000Alpha80,

    accentBlue = HowzappBlue,
    accentPurple = HowzappPurple,
    accentViolet = HowzappViolet,
    accentPink = HowzappPink,
    accentOrange = HowzappOrangeDark,
    accentYellow = HowzappYellow,
    accentGreen = HowzappAccentGreen,
    accentRed = HowzappAccentRed,
    accentTeal = HowzappTeal,
    accentLightBlue = HowzappLightBlue,
    accentGrey = HowzappGrey,

    cakeViolet = HowzappCakeDarkViolet,
    cakeGreen = HowzappCakeDarkGreen,
    cakeBlue = HowzappSkyBlueDark,
    cakePink = HowzappCakeDarkPink,
    cakeOrange = HowzappCakeDarkOrange,
    cakeYellow = HowzappCakeDarkYellow,
    cakeTeal = HowzappCakeDarkTeal,
    cakePurple = HowzappCakeDarkPurple,
    cakeRed = HowzappCakeDarkRed,
    cakeMint = HowzappCakeDarkMint,
)

val LightColorScheme = lightColorScheme(
    primary = HowzappPurple2Light,
    onPrimary = HowzappBrand1000,
    primaryContainer = HowzappBrand100,
    onPrimaryContainer = HowzappBrand900,

    secondary = HowzappPurple1Light,
    onSecondary = Color.Black,
    secondaryContainer = HowzappBase100,
    onSecondaryContainer = HowzappBase900,

    tertiary = HowzappBrand900,
    onTertiary = HowzappBase0,
    tertiaryContainer = HowzappBrand100,
    onTertiaryContainer = HowzappBrand1000,

    error = HowzappRed500,
    onError = HowzappBase0,
    errorContainer = HowzappRed200,
    onErrorContainer = HowzappRed600,

    background = HowzappNeutralWhite,
    onBackground = HowzappBase0,
    surface = HowzappNeutral1Light,
    onSurface = HowzappBase1000,
    surfaceVariant = HowzappBase100,
    onSurfaceVariant = HowzappBase900,

    outline = HowzappBase1000Alpha8,
    outlineVariant = HowzappBase200,
)

val DarkColorScheme = darkColorScheme(
    primary = HowzappPurple2Dark,
    onPrimary = Color.White,
    primaryContainer = HowzappBrand900,
    onPrimaryContainer = HowzappBrand500,

    secondary = HowzappPurple1Dark,
    onSecondary = Color.White,
    secondaryContainer = HowzappBase900,
    onSecondaryContainer = HowzappBase150,

    tertiary = HowzappBrand500,
    onTertiary = HowzappBase1000,
    tertiaryContainer = HowzappBrand900,
    onTertiaryContainer = HowzappBrand500,

    error = HowzappRed500,
    onError = HowzappBase0,
    errorContainer = HowzappRed600,
    onErrorContainer = HowzappRed200,

    background = HowzappNeutralBlack,
    onBackground = HowzappBase0,
    surface = HowzappNeutral1Dark,
    onSurface = HowzappBase0,
    surfaceVariant = HowzappBase900,
    onSurfaceVariant = HowzappBase150,

    outline = HowzappBase100Alpha10,
    outlineVariant = HowzappBase800,
)