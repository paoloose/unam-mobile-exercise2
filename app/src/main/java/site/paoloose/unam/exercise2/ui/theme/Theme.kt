package site.paoloose.unam.exercise2.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import site.paoloose.unam.exercise2.ui.viewmodel.SettingsViewModel
import site.paoloose.unam.exercise2.ui.viewmodel.ThemeMode

private val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp)
)

private fun greenColorScheme() = darkColorScheme(
    primary = GreenBright, onPrimary = AppWhite,
    primaryContainer = GreenBase, onPrimaryContainer = GreenDark,
    secondary = AppWhite, onSecondary = GreenDark,
    secondaryContainer = GreenBase, onSecondaryContainer = AppWhite,
    tertiary = VioletBright, onTertiary = AppWhite,
    tertiaryContainer = VioletDark, onTertiaryContainer = VioletAccent,
    error = RedBright, onError = AppWhite,
    errorContainer = RedDark, onErrorContainer = RedAccent,
    background = GreenDarker, onBackground = AppWhite,
    surface = GreenDarker, onSurface = AppWhite,
    surfaceVariant = GreenDark, onSurfaceVariant = AppWhite,
    outline = GreenBase
)

private fun blueColorScheme() = darkColorScheme(
    primary = BlueBright, onPrimary = AppWhite,
    primaryContainer = BlueBase, onPrimaryContainer = BlueDark,
    secondary = AppWhite, onSecondary = BlueDark,
    secondaryContainer = BlueBase, onSecondaryContainer = AppWhite,
    tertiary = VioletBright, onTertiary = AppWhite,
    tertiaryContainer = VioletDark, onTertiaryContainer = VioletAccent,
    error = RedBright, onError = AppWhite,
    errorContainer = RedDark, onErrorContainer = RedAccent,
    background = BlueDark, onBackground = AppWhite,
    surface = BlueDark, onSurface = AppWhite,
    surfaceVariant = BlueDark.copy(alpha = 0.8f), onSurfaceVariant = AppWhite,
    outline = BlueBase
)

private fun redColorScheme() = darkColorScheme(
    primary = RedBright, onPrimary = AppWhite,
    primaryContainer = RedBase, onPrimaryContainer = RedDark,
    secondary = AppWhite, onSecondary = RedDark,
    secondaryContainer = RedBase, onSecondaryContainer = AppWhite,
    tertiary = BlueBright, onTertiary = AppWhite,
    tertiaryContainer = BlueDark, onTertiaryContainer = BlueBright,
    error = RedBright, onError = AppWhite,
    errorContainer = RedDark, onErrorContainer = RedAccent,
    background = RedDark, onBackground = AppWhite,
    surface = RedDark, onSurface = AppWhite,
    surfaceVariant = RedDark.copy(alpha = 0.8f), onSurfaceVariant = AppWhite,
    outline = RedBase
)

private fun violetColorScheme() = darkColorScheme(
    primary = VioletBright, onPrimary = AppWhite,
    primaryContainer = VioletBase, onPrimaryContainer = VioletDark,
    secondary = AppWhite, onSecondary = VioletDark,
    secondaryContainer = VioletBase, onSecondaryContainer = AppWhite,
    tertiary = GreenBright, onTertiary = AppWhite,
    tertiaryContainer = GreenDark, onTertiaryContainer = GreenBright,
    error = RedBright, onError = AppWhite,
    errorContainer = RedDark, onErrorContainer = RedAccent,
    background = VioletDark, onBackground = AppWhite,
    surface = VioletDark, onSurface = AppWhite,
    surfaceVariant = VioletDark.copy(alpha = 0.8f), onSurfaceVariant = AppWhite,
    outline = VioletBase
)

@Composable
fun Exercise2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    settingsViewModel: SettingsViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    val themeMode by settingsViewModel.themeMode.collectAsState()

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> when (themeMode) {
            ThemeMode.GREEN -> greenColorScheme()
            ThemeMode.BLUE -> blueColorScheme()
            ThemeMode.RED -> redColorScheme()
            ThemeMode.VIOLET -> violetColorScheme()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
