package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val StudioDarkColorScheme = darkColorScheme(
    primary = ElectricPurple,
    onPrimary = StudioDark950,
    primaryContainer = ElectricPurpleDark,
    onPrimaryContainer = StudioDark50,
    secondary = SoftCyan,
    onSecondary = StudioDark950,
    secondaryContainer = StudioDark800,
    onSecondaryContainer = SoftCyan,
    tertiary = StudioIndigo,
    onTertiary = Color.White,
    background = StudioDark950,
    onBackground = StudioDark100,
    surface = StudioDark900,
    onSurface = StudioDark100,
    surfaceVariant = StudioDark850,
    onSurfaceVariant = StudioDark300,
    outline = StudioDark700,
    outlineVariant = StudioDark800,
    error = StudioAlert,
    onError = Color.White
)

private val StudioLightColorScheme = lightColorScheme(
    primary = ElectricPurple,
    onPrimary = Color.White,
    primaryContainer = ElectricPurpleLight,
    onPrimaryContainer = StudioDark900,
    secondary = SoftCyanDark,
    onSecondary = Color.White,
    secondaryContainer = StudioDark200,
    onSecondaryContainer = StudioDark900,
    tertiary = StudioIndigo,
    onTertiary = Color.White,
    background = StudioDark50,
    onBackground = StudioDark950,
    surface = Color.White,
    onSurface = StudioDark900,
    surfaceVariant = StudioDark100,
    onSurfaceVariant = StudioDark500,
    outline = StudioDark300,
    outlineVariant = StudioDark200,
    error = StudioAlert,
    onError = Color.White
)

@Composable
fun BriefDoctorTheme(
    darkTheme: Boolean = true, // Default to sleek creative agency dark mode
    dynamicColor: Boolean = false, // Keep crisp branded agency palette consistent
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> StudioDarkColorScheme
        else -> StudioLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
