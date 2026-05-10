package site.paoloose.unam.exercise2.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.paoloose.unam.exercise2.R
import site.paoloose.unam.exercise2.data.remote.ApiClient
import site.paoloose.unam.exercise2.data.remote.dto.TeamVenueDto
import site.paoloose.unam.exercise2.data.repository.TeamsRepository

sealed class TeamsUiState {
    object Loading : TeamsUiState()
    data class Success(val teams: List<TeamVenueDto>) : TeamsUiState()
    data class Error(val message: String) : TeamsUiState()
}

class TeamsViewModel(application: Application) : AndroidViewModel(application) {
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
                    _uiState.value = TeamsUiState.Error(
                        error.localizedMessage
                            ?: getApplication<Application>().getString(R.string.error_unknown)
                    )
                }
            )
        }
    }
}
