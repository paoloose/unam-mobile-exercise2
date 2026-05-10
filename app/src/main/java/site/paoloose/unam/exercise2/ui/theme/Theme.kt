package site.paoloose.unam.exercise2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import site.paoloose.unam.exercise2.ui.viewmodel.SettingsViewModel
import site.paoloose.unam.exercise2.ui.viewmodel.ThemeMode

private val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp),
    extraLarge = RoundedCornerShape(8.dp)
)

private val DarkColorScheme = darkColorScheme(
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

private val LightColorScheme = lightColorScheme(
    primary = GreenBright, onPrimary = AppWhite,
    primaryContainer = LightPrimaryContainer, onPrimaryContainer = GreenDarker,
    secondary = GreenDark, onSecondary = AppWhite,
    secondaryContainer = GreenBase, onSecondaryContainer = GreenDarker,
    tertiary = VioletDark, onTertiary = AppWhite,
    tertiaryContainer = VioletBright, onTertiaryContainer = VioletDark,
    error = RedBright, onError = AppWhite,
    errorContainer = RedAccent, onErrorContainer = RedDark,
    background = AppWhite, onBackground = GreenDarker,
    surface = AppWhite, onSurface = GreenDarker,
    surfaceVariant = LightSurfaceVariant, onSurfaceVariant = LightOnSurfaceVariant,
    outline = GreenDark
)

@Composable
fun Exercise2Theme(
    dynamicColor: Boolean = false,
    settingsViewModel: SettingsViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    val themeMode by settingsViewModel.themeMode.collectAsState()
    val context = LocalContext.current

    DisposableEffect(themeMode) {
        val window = (context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = themeMode == ThemeMode.LIGHT
        onDispose { }
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (themeMode == ThemeMode.DARK) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        themeMode == ThemeMode.DARK -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
