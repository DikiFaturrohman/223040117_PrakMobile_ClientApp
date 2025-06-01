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

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Definisikan LightColorScheme menggunakan warna kustom kita
private val LightColorScheme = lightColorScheme(
    primary = EWasteGreen,
    onPrimary = EWasteWhite,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = EWasteGreen,      // Latar belakang utama (hijau)
    surface = EWasteWhite,         // Warna kartu (putih)
    onBackground = EWasteWhite,    // Teks di atas latar belakang utama
    onSurface = EWasteBlack,       // Teks di atas kartu
    onSurfaceVariant = EWasteGray  // Warna untuk teks sekunder/abu-abu
)

@Composable
fun EwasteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Dinonaktifkan agar tema kustom kita yang dipakai
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme // Gunakan LightColorScheme kustom kita
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}