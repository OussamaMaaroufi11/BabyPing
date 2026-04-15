package com.app.babyroutine.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BabyBlue,
    onPrimary = Color(0xFF062B30),

    secondary = BabyMint,
    onSecondary = Color(0xFF102318),

    secondaryContainer = Color(0xFFC8F3D2),
    onSecondaryContainer = Color(0xFF173222),

    tertiary = Color(0xFFF8EEFF),
    onTertiary = Color(0xFF30263A),

    background = LightBg,
    onBackground = Color(0xFF1F2430),

    surface = Color.White,
    onSurface = Color(0xFF1F2430),

    surfaceVariant = LightSurfaceSoft,
    onSurfaceVariant = Color(0xFF626B7A),

    outline = LightOutline,

    error = Color(0xFFFF6B6B),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = BabyBlue,
    onPrimary = Color(0xFF04131A),

    secondary = BabyMint,
    onSecondary = Color(0xFF0A1B12),

    secondaryContainer = Color(0xFF243B33),
    onSecondaryContainer = Color(0xFFDDF8E7),

    tertiary = BabyLavender,
    onTertiary = Color(0xFF151028),

    background = DarkBg,
    onBackground = DarkTextPrimary,

    surface = DarkSurface,
    onSurface = DarkTextPrimary,

    surfaceVariant = DarkSurfaceSoft,
    onSurfaceVariant = DarkTextSecondary,

    outline = DarkOutline,

    error = Color(0xFFFF7B7B),
    onError = Color(0xFF2A0C0C)
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