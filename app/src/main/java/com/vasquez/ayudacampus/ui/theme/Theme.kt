package com.vasquez.ayudacampus.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF22B8CF),
    onPrimary = Color(0xFF00363E),
    primaryContainer = Color(0xFF164E57),
    onPrimaryContainer = Color(0xFFB7F0F5),
    secondary = Color(0xFFA78BFA),
    onSecondary = Color(0xFF25154A),
    secondaryContainer = Color(0xFF3B2B64),
    onSecondaryContainer = Color(0xFFEDE4FF),
    background = Color(0xFF101418),
    onBackground = Color(0xFFE5E7EB),
    surface = Color(0xFF191D23),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF252A31),
    onSurfaceVariant = Color(0xFFB8C0CC)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0891B2),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCFFAFE),
    onPrimaryContainer = Color(0xFF164E63),
    secondary = Color(0xFF7C3AED),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE4FF),
    onSecondaryContainer = Color(0xFF4C1D95),
    background = Color(0xFFF7FDFE),
    onBackground = Color(0xFF1F2937),
    surface = Color.White,
    onSurface = Color(0xFF1F2937),
    surfaceVariant = Color(0xFFF0F2F5),
    onSurfaceVariant = Color(0xFF596579)
)

@Composable
fun AyudaCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
