package de.memorian.wearos.marsrover.app.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

val md_theme_primary = Color(0xFF924C00)
val md_theme_onPrimary = Color(0xFFFFFFFF)
val md_theme_secondary = Color(0xFF845400)
val md_theme_onSecondary = Color(0xFFFFFFFF)
val md_theme_error = Color(0xFFBA1A1A)
val md_theme_onError = Color(0xFFFFFFFF)
val md_theme_background = Color(0xFF451804)
val md_theme_onBackground = Color(0xFF201A17)
val md_theme_surface = Color(0xFF451804)
val md_theme_onSurface = Color(0xFF201A17)
val md_theme_onSurfaceVariant = Color(0xFFf0e7e7)

private val wearColorPalette = Colors(
    primary = md_theme_primary,
    onPrimary = md_theme_onPrimary,
    secondary = md_theme_secondary,
    onSecondary = md_theme_onSecondary,
    error = md_theme_error,
    onError = md_theme_onError,
    background = md_theme_background,
    onBackground = md_theme_onBackground,
    surface = md_theme_surface,
    onSurface = md_theme_onSurface,
    onSurfaceVariant = md_theme_onSurfaceVariant,
)

@Composable
fun MarsRoverTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = wearColorPalette,
        content = content
    )
}