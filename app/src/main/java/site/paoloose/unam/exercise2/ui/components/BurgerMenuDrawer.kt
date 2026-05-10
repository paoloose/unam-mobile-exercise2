package site.paoloose.unam.exercise2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import site.paoloose.unam.exercise2.R
import site.paoloose.unam.exercise2.ui.viewmodel.ApiSource
import site.paoloose.unam.exercise2.ui.viewmodel.SettingsViewModel
import site.paoloose.unam.exercise2.ui.viewmodel.ThemeMode

private val DrawerItemShape = RoundedCornerShape(8.dp)

@Composable
private fun DrawerItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(DrawerItemShape)
            .clickable(onClick = onClick)
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BurgerMenuDrawer(
    drawerState: androidx.compose.material3.DrawerState,
    settingsViewModel: SettingsViewModel,
    gesturesEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    LanguageSection()
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    ApiSourceSection(settingsViewModel)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    ThemeSection(settingsViewModel)
                }
            }
        },
        content = content
    )
}

@Composable
private fun LanguageSection() {
    val currentLocale = LocalConfiguration.current.locales[0].language

    val languages = remember {
        listOf(
            DrawerLanguage("es", R.string.lang_es),
            DrawerLanguage("fr", R.string.lang_fr),
            DrawerLanguage("en", R.string.lang_en),
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Language,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.menu_language),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    languages.forEach { lang ->
        DrawerItem(
            label = stringResource(lang.labelRes),
            selected = currentLocale == lang.code,
            onClick = {
                val appLocale = LocaleListCompat.forLanguageTags(lang.code)
                androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(appLocale)
            },
            modifier = Modifier.padding(vertical = 2.dp)
        )
    }
}

@Composable
private fun ApiSourceSection(settingsViewModel: SettingsViewModel) {
    val currentSource by settingsViewModel.apiSource.collectAsState()

    val sources = listOf(
        ApiSource.API_FOOTBALL to R.string.source_api_footbal,
        ApiSource.COMPUTOMOVIL_MOCK to R.string.source_computomovil_mock,
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.SwapHoriz,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.menu_api_source),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    sources.forEach { (source, labelRes) ->
        DrawerItem(
            label = stringResource(labelRes),
            selected = currentSource == source,
            onClick = { settingsViewModel.setApiSource(source) },
            modifier = Modifier.padding(vertical = 2.dp)
        )
    }
}

@Composable
private fun ThemeSection(settingsViewModel: SettingsViewModel) {
    val currentTheme by settingsViewModel.themeMode.collectAsState()

    val themes = listOf(
        ThemeMode.DARK to R.string.theme_dark,
        ThemeMode.LIGHT to R.string.theme_light
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.DarkMode,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.menu_theme),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    themes.forEach { (mode, labelRes) ->
        DrawerItem(
            label = stringResource(labelRes),
            selected = currentTheme == mode,
            onClick = { settingsViewModel.setThemeMode(mode) },
            modifier = Modifier.padding(vertical = 2.dp)
        )
    }
}

private data class DrawerLanguage(val code: String, @param:androidx.annotation.StringRes val labelRes: Int)
