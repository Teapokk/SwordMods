package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SwordModsColorScheme = darkColorScheme(
    primary = SwordRed,
    onPrimary = TextWhite,
    primaryContainer = SlateSurfaceVariant,
    onPrimaryContainer = SwordRed,
    secondary = SwordGold,
    onSecondary = SlateBackground,
    secondaryContainer = SlateSurfaceVariant,
    onSecondaryContainer = SwordGold,
    tertiary = SwordEmerald,
    onTertiary = TextWhite,
    background = SlateBackground,
    onBackground = TextWhite,
    surface = SlateSurface,
    onSurface = TextWhite,
    surfaceVariant = SlateSurfaceVariant,
    onSurfaceVariant = TextGray,
    outline = SlateCardBorder,
    outlineVariant = SlateCardBorder,
    error = SwordRed,
    onError = TextWhite
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = SlateBackground.toArgb()
                window.navigationBarColor = SlateBackground.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = SwordModsColorScheme,
        typography = Typography,
        content = content
    )
}
