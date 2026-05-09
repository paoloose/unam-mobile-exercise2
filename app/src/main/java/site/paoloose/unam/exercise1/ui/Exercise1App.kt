package site.paoloose.unam.exercise1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import site.paoloose.unam.exercise1.R
import site.paoloose.unam.exercise1.ui.components.LanguageSelector
import site.paoloose.unam.exercise1.ui.screens.HomeScreen
import site.paoloose.unam.exercise1.ui.theme.Exercise1Theme

import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.collectAsState
import site.paoloose.unam.exercise1.network.NetworkMonitor

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun Exercise1App() {
    val context = LocalContext.current
    val networkMonitor = remember { NetworkMonitor(context) }

    val isConnected by networkMonitor.isConnected.collectAsState(initial = true)

    var selectedTeamId by rememberSaveable { mutableStateOf<Int?>(null) }
    var showSplash by rememberSaveable { mutableStateOf(true) }

    if (showSplash) {
        site.paoloose.unam.exercise1.ui.screens.AnimatedSplashScreen(
            onSplashFinished = { showSplash = false }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(R.drawable.cup_logo),
                            contentDescription = stringResource(R.string.cd_cup_logo),
                            modifier = Modifier.padding(end = 8.dp).size(32.dp)
                        )
                        Text(stringResource(R.string.app_name))
                    }
                },
                navigationIcon = {
                    if (selectedTeamId != null) {
                        androidx.compose.material3.IconButton(onClick = { selectedTeamId = null }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.cd_back)
                            )
                        }
                    }
                },
                actions = {
                    LanguageSelector()
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (selectedTeamId != null) {
                androidx.activity.compose.BackHandler {
                    selectedTeamId = null
                }
                site.paoloose.unam.exercise1.ui.screens.SquadScreen(
                    teamId = selectedTeamId!!,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                HomeScreen(
                    onTeamClick = { selectedTeamId = it },
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
    Exercise1Theme {
        Greeting(stringResource(R.string.default_name))
    }
}
