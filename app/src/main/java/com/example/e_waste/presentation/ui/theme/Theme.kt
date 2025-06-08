package com.example.e_waste.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Skema warna untuk Tema Terang (Light Mode)
private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    secondary = DarkGray,
    onSecondary = White,
    background = LightGray,
    surface = White,
    onBackground = Black,
    onSurface = Black,
    onSurfaceVariant = Gray,
    outline = Gray.copy(alpha = 0.5f)
)

// Skema warna untuk Tema Gelap (Dark Mode)
private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    secondary = LightGray,
    onSecondary = Black,
    background = Black,
    surface = DarkGray,
    onBackground = White,
    onSurface = White,
    onSurfaceVariant = Gray,
    outline = Gray.copy(alpha = 0.5f)
)

@Composable
fun EwasteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}