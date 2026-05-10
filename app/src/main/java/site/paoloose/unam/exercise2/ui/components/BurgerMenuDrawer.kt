package site.paoloose.unam.exercise2.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import site.paoloose.unam.exercise2.R
import site.paoloose.unam.exercise2.ui.viewmodel.ApiSource
import site.paoloose.unam.exercise2.ui.viewmodel.SettingsViewModel

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
            DrawerLanguage("fr", R.string.lang_fr),
            DrawerLanguage("en", R.string.lang_en),
            DrawerLanguage("es", R.string.lang_es)
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
        NavigationDrawerItem(
            label = { Text(stringResource(lang.labelRes)) },
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
        ApiSource.COMPUTOMOVIL_MOCK to R.string.source_computomovil_mock,
        ApiSource.API_FOOTBALL to R.string.source_api_footbal
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
        NavigationDrawerItem(
            label = { Text(stringResource(labelRes)) },
            selected = currentSource == source,
            onClick = { settingsViewModel.setApiSource(source) },
            modifier = Modifier.padding(vertical = 2.dp)
        )
    }
}

private data class DrawerLanguage(val code: String, @param:androidx.annotation.StringRes val labelRes: Int)
