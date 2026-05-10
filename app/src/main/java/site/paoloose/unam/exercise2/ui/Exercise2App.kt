package site.paoloose.unam.exercise2.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import site.paoloose.unam.exercise2.R
import site.paoloose.unam.exercise2.network.NetworkMonitor
import site.paoloose.unam.exercise2.ui.components.BurgerMenuDrawer
import site.paoloose.unam.exercise2.ui.screens.AnimatedSplashScreen
import site.paoloose.unam.exercise2.ui.screens.HomeScreen
import site.paoloose.unam.exercise2.ui.screens.SquadScreen
import site.paoloose.unam.exercise2.ui.theme.Exercise2Theme
import site.paoloose.unam.exercise2.ui.viewmodel.ApiSource
import site.paoloose.unam.exercise2.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun Exercise2App() {
    val context = LocalContext.current
    val networkMonitor = remember { NetworkMonitor(context) }
    val settingsViewModel: SettingsViewModel = viewModel()

    val isConnected by networkMonitor.isConnected.collectAsState(initial = true)
    val apiSource by settingsViewModel.apiSource.collectAsState()

    var selectedTeamId by rememberSaveable { mutableStateOf<Int?>(null) }
    var showSplash by rememberSaveable { mutableStateOf(true) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    if (showSplash) {
        AnimatedSplashScreen(
            onSplashFinished = { showSplash = false }
        )
    } else {
        BurgerMenuDrawer(
            drawerState = drawerState,
            settingsViewModel = settingsViewModel,
            gesturesEnabled = selectedTeamId == null
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(R.drawable.cup_logo),
                                        contentDescription = stringResource(R.string.cd_cup_logo),
                                        modifier = Modifier.padding(end = 8.dp).size(32.dp)
                                    )
                                    Text(stringResource(R.string.app_name))
                                }
                            },
                            navigationIcon = {
                                if (selectedTeamId != null) {
                                    IconButton(onClick = { selectedTeamId = null }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = stringResource(R.string.cd_back)
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = stringResource(R.string.menu_open)
                                        )
                                    }
                                }
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        if (selectedTeamId != null) {
                            BackHandler {
                                selectedTeamId = null
                            }
                            SquadScreen(
                                teamId = selectedTeamId!!,
                                useFootballApi = apiSource == ApiSource.API_FOOTBALL,
                                isConnected = isConnected,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            HomeScreen(
                                onTeamClick = { selectedTeamId = it },
                                isConnected = isConnected,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = !isConnected,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.internet_connection_lost),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.greeting_text, name),
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Exercise2Theme {
        Greeting(stringResource(R.string.default_name))
    }
}
