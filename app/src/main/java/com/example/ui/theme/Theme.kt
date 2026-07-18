package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NaturalLeafLight,
    onPrimary = Color.Black,
    primaryContainer = NaturalDarkHighlight,
    onPrimaryContainer = NaturalDarkText,
    secondary = NaturalDarkLine,
    onSecondary = NaturalSageLight,
    tertiary = NaturalLeafLight,
    background = NaturalDarkBg,
    onBackground = NaturalDarkText,
    surface = NaturalDarkCard,
    onSurface = NaturalDarkText,
    surfaceVariant = NaturalDarkHighlight,
    onSurfaceVariant = NaturalDarkText
)

private val LightColorScheme = lightColorScheme(
    primary = NaturalForestGreen,
    onPrimary = Color.White,
    primaryContainer = NaturalHighlightGreen,
    onPrimaryContainer = NaturalLightText,
    secondary = NaturalNotebookLine,
    onSecondary = NaturalSageMuted,
    tertiary = NaturalForestGreen,
    background = NaturalLightBg,
    onBackground = NaturalLightText,
    surface = Color.White,
    onSurface = NaturalLightText,
    surfaceVariant = NaturalGrassLight,
    onSurfaceVariant = NaturalLightText
)


@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
