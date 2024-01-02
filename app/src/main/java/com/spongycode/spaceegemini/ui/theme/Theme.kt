package com.spongycode.spaceegemini.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    primaryContainer = Color(0xFF1D1C1C),
    secondaryContainer = Color(0xFF353434),
    inversePrimary = Color(0xFFD8D3D3),
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color.Black,

//    primary = ThemeColors.Dark.primary,
//    onPrimary = ThemeColors.Dark.text,
//    surface = ThemeColors.Dark.surface,
//    background = ThemeColors.Dark.background
    )

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
//    primaryContainer = Color(0xFFEBE7E7),
    primaryContainer = Color.White,
//    secondaryContainer = Color(0xFFE2D8D8),
    secondaryContainer = Color.LightGray,
    inversePrimary = Color(0xFF777373),
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color.White,

    /* Other default colors to override
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

//    primary = ThemeColors.Light.primary,
//    onPrimary = ThemeColors.Light.text,
//    surface = ThemeColors.Light.surface,
//    background = ThemeColors.Light.background
)

@Composable
fun SpaceeGeminiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if (darkTheme) Color.Black.toArgb() else Color.Gray.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}