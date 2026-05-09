package site.paoloose.unam.exercise1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.paoloose.unam.exercise1.data.remote.ApiClient
import site.paoloose.unam.exercise1.data.remote.dto.TeamVenueDto
import site.paoloose.unam.exercise1.data.repository.TeamsRepository

sealed class TeamsUiState {
    object Loading : TeamsUiState()
    data class Success(val teams: List<TeamVenueDto>) : TeamsUiState()
    data class Error(val message: String) : TeamsUiState()
}

class TeamsViewModel : ViewModel() {
    private val repository = TeamsRepository(ApiClient.worldCupApiService)

    private val _uiState = MutableStateFlow<TeamsUiState>(TeamsUiState.Loading)
    val uiState: StateFlow<TeamsUiState> = _uiState.asStateFlow()

    init {
        fetchTeams()
    }

    fun fetchTeams() {
        viewModelScope.launch {
            _uiState.value = TeamsUiState.Loading
            val result = repository.getTeams()
            result.fold(
                onSuccess = { teams ->
                    _uiState.value = TeamsUiState.Success(teams)
                },
                onFailure = { error ->
                    _uiState.value = TeamsUiState.Error(error.localizedMessage ?: "Unknown error occurred")
                }
            )
        }
    }
}
