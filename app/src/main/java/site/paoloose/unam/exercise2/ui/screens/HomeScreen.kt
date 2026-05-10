package site.paoloose.unam.exercise2.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import site.paoloose.unam.exercise2.ui.theme.AppBlack
import site.paoloose.unam.exercise2.ui.theme.GoldDark
import site.paoloose.unam.exercise2.ui.theme.GoldBright
import site.paoloose.unam.exercise2.ui.theme.RedAccent
import site.paoloose.unam.exercise2.ui.theme.RedDark
import site.paoloose.unam.exercise2.R
import site.paoloose.unam.exercise2.data.remote.dto.TeamVenueDto
import site.paoloose.unam.exercise2.ui.viewmodel.TeamsUiState
import site.paoloose.unam.exercise2.ui.viewmodel.TeamsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onTeamClick: (Int) -> Unit,
    isConnected: Boolean = true,
    modifier: Modifier = Modifier,
    viewModel: TeamsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var wasDisconnected by remember { mutableStateOf(false) }
    LaunchedEffect(isConnected) {
        if (isConnected && wasDisconnected) {
            viewModel.fetchTeams()
        }
        wasDisconnected = !isConnected
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Banner item
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // A bit taller to showcase the image and gradient
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner),
                    contentDescription = stringResource(R.string.cd_banner),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Degraded black gradient fading into the background color
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    AppBlack.copy(alpha = 0.4f),
                                    AppBlack.copy(alpha = 0.8f) // not full black
                                ),
                                // 200dp in pixels is roughly where we want to start the transition
                                startY = 150f
                            )
                        )
                )
            }
        }

        // Sticky header
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.title_meet_teams),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                // Reload data button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { viewModel.fetchTeams() }
                ) {
                    IconButton(onClick = { viewModel.fetchTeams() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.cd_refresh),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }

        // That's how we handle differents data states
        when (val state = uiState) {
            is TeamsUiState.Loading -> {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            is TeamsUiState.Error -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.error_failed_teams),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchTeams() }) {
                            Text(stringResource(R.string.btn_retry))
                        }
                    }
                }
            }
            is TeamsUiState.Success -> {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                items(state.teams) { teamVenue ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        TeamVenueCard(
                            teamVenue = teamVenue,
                            onClick = { onTeamClick(teamVenue.team.id) }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun TeamVenueCard(teamVenue: TeamVenueDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Team flag
                AsyncImage(
                    model = teamVenue.team.logo,
                    contentDescription = stringResource(R.string.cd_flag, teamVenue.team.name),
                    modifier = Modifier
                        .width(64.dp)
                        .height(48.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Team details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = teamVenue.team.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (teamVenue.team.code != null) {
                            // Red Code Tag (Dark Red BG, Light Red Text)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                     .background(RedDark)
                                     .padding(horizontal = 6.dp, vertical = 2.dp)
                             ) {
                                 Text(
                                     text = teamVenue.team.code,
                                     style = MaterialTheme.typography.labelMedium,
                                     fontWeight = FontWeight.Bold,
                                     color = RedAccent
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        if (teamVenue.team.founded != null) {
                            Text(
                                text = stringResource(R.string.label_founded, teamVenue.team.founded),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Venue section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(R.string.cd_venue),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = teamVenue.venue.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = teamVenue.venue.city,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Capacity Tag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GoldDark) // Dark Gold
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.capacity_format, teamVenue.venue.capacity),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldBright // Bright Gold
                        )
                    }
                }
            }
        }
    }
}
