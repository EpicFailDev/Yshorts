package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.domain.model.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = TubeMasterRed,
    onPrimary = TubeMasterWhite,
    primaryContainer = TubeMasterRedDim,
    onPrimaryContainer = TubeMasterWhite,
    secondary = TubeMasterGray,
    onSecondary = TubeMasterBg,
    background = TubeMasterBg,
    onBackground = TubeMasterWhite,
    surface = TubeMasterCard,
    onSurface = TubeMasterWhite,
    surfaceVariant = TubeMasterCardHover,
    onSurfaceVariant = TubeMasterGray,
    outline = TubeMasterBorder,
    outlineVariant = Color(0xFF1E1E1E)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE4E6),
    onPrimaryContainer = PrimaryRedDark,
    secondary = Color(0xFF3F3F46),
    onSecondary = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight,
    outlineVariant = Color(0xFFF4F4F5)
)

@Composable
fun ShortsScriptTheme(
    themeMode: ThemeMode = ThemeMode.AUTO,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.AUTO -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    ShortsScriptTheme(content = content)
}
