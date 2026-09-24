package com.example.ui.theme

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

private val DarkColorScheme =
  darkColorScheme(
    primary = FairwayGreen80,
    secondary = FairwayGold80,
    tertiary = FairwayTertiary80,
    background = GolfDarkSurface,
    surface = GolfDarkSurfaceElevated,
    onPrimary = Color(0xFF003915),
    onSecondary = Color(0xFF422C00),
    onBackground = Color(0xFFE8EFE8),
    onSurface = Color(0xFFE8EFE8)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GolfGreenPrimary,
    secondary = FairwayGold40,
    tertiary = GolfGreenLight,
    background = GolfTurfBg,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1B241C),
    onSurface = Color(0xFF1B241C),
    surfaceVariant = Color(0xFFE2EBE2),
    onSurfaceVariant = Color(0xFF3B483D)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve our custom golf theme branding
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
