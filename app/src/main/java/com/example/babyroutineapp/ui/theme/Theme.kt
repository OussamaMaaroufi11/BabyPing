package com.example.babyroutineapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF57C8D4),
    onPrimary = Color(0xFF062B30),

    secondary = Color(0xFF8ED9A8),
    onSecondary = Color(0xFF102318),

    secondaryContainer = Color(0xFFAEE8A8),
    onSecondaryContainer = Color(0xFF1D1B20),

    tertiary = Color(0xFFF3E9FF),
    onTertiary = Color(0xFF2A2230),

    background = Color(0xFFF7F2FF),
    onBackground = Color(0xFF1F1B24),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1F1B24),

    surfaceVariant = Color(0xFFF2DDCC),
    onSurfaceVariant = Color(0xFF5A5361),

    outline = Color(0xFFD0C4BE)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF71E4E0),
    onPrimary = Color(0xFF082B2B),

    secondary = Color(0xFF8ED9A8),
    onSecondary = Color(0xFF0E2116),

    secondaryContainer = Color(0xFF5A536B),
    onSecondaryContainer = Color(0xFFF4EFFA),

    tertiary = Color(0xFF2B1F36),
    onTertiary = Color(0xFFF1E8F8),

    background = Color(0xFF10131A),
    onBackground = Color(0xFFF3F2F7),

    surface = Color(0xFF1A1F29),
    onSurface = Color(0xFFF3F2F7),

    surfaceVariant = Color(0xFF222A35),
    onSurfaceVariant = Color(0xFFC7D0DD),

    outline = Color(0xFF556070)
)

@Composable
fun BabyRoutineAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}