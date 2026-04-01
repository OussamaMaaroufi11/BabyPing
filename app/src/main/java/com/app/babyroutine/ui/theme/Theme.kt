package com.app.babyroutine.ui.theme

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
    primary = Color(0xFF00E5FF),
    onPrimary = Color.Black,

    secondary = Color(0xFF7CFFB2),
    onSecondary = Color.Black,

    secondaryContainer = Color(0xFF23324A),
    onSecondaryContainer = Color(0xFFEAF2FF),

    background = Color(0xFF101827),
    onBackground = Color(0xFFEAF2FF),

    surface = Color(0xFF182235),
    onSurface = Color(0xFFEAF2FF),

    surfaceVariant = Color(0xFF22314A),
    onSurfaceVariant = Color(0xFFBFD0EA),

    outline = Color(0xFF415574),

    tertiary = Color(0xFF8F7CFF),
    onTertiary = Color.White,

    error = Color(0xFFFF6B6B),
    onError = Color.Black
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