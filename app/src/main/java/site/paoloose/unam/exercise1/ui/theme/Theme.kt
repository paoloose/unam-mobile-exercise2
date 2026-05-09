package site.paoloose.unam.exercise1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp)
)

private val DarkColorScheme = darkColorScheme(
    primary = GreenBright,
    onPrimary = AppWhite,
    primaryContainer = GreenBase,
    onPrimaryContainer = GreenDark,

    secondary = AppWhite,
    onSecondary = GreenDark,
    secondaryContainer = GreenBase,
    onSecondaryContainer = AppWhite,

    tertiary = VioletBright,
    onTertiary = AppWhite,
    tertiaryContainer = VioletDark,
    onTertiaryContainer = VioletAccent,

    error = RedBright,
    onError = AppWhite,
    errorContainer = RedDark,
    onErrorContainer = RedAccent,

    background = GreenDarker,
    onBackground = AppWhite,
    surface = GreenDarker,
    onSurface = AppWhite,

    surfaceVariant = GreenDark,
    onSurfaceVariant = AppWhite,
    outline = GreenBase
)

private val LightColorScheme = lightColorScheme(
    primary = GreenBright,
    onPrimary = AppWhite,
    primaryContainer = GreenBase,
    onPrimaryContainer = GreenDark,

    secondary = AppWhite,
    onSecondary = GreenDark,
    secondaryContainer = GreenBase,
    onSecondaryContainer = AppWhite,

    tertiary = VioletBright,
    onTertiary = AppWhite,
    tertiaryContainer = VioletDark,
    onTertiaryContainer = VioletAccent,

    error = RedBright,
    onError = AppWhite,
    errorContainer = RedDark,
    onErrorContainer = RedAccent,

    background = GreenDarker,
    onBackground = AppWhite,
    surface = GreenDarker,
    onSurface = AppWhite,

    surfaceVariant = GreenDark,
    onSurfaceVariant = AppWhite,
    outline = GreenBase
)

@Composable
fun Exercise1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // dynamic colors fo accesibility, not tested yet
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
