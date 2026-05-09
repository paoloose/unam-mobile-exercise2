package site.paoloose.unam.exercise1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.paoloose.unam.exercise1.data.remote.ApiClient
import site.paoloose.unam.exercise1.data.remote.dto.TeamSquadDto
import site.paoloose.unam.exercise1.data.repository.SquadRepository

sealed class SquadUiState {
    object Loading : SquadUiState()
    data class Success(val squad: TeamSquadDto) : SquadUiState()
    data class Error(val message: String) : SquadUiState()
}

class SquadViewModel : ViewModel() {
    private val repository = SquadRepository(ApiClient.worldCupApiService)
    private val _uiState = MutableStateFlow<SquadUiState>(SquadUiState.Loading)
    val uiState: StateFlow<SquadUiState> = _uiState.asStateFlow()

    fun fetchSquad(teamId: Int) {
        viewModelScope.launch {
            _uiState.value = SquadUiState.Loading
            val result = repository.getSquad(teamId)
            result.fold(
                onSuccess = { squad ->
                    _uiState.value = SquadUiState.Success(squad)
                },
                onFailure = { error ->
                    _uiState.value = SquadUiState.Error(error.localizedMessage ?: "Unknown error")
                }
            )
        }
    }
}
