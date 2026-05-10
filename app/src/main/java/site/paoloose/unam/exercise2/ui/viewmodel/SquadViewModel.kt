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
import site.paoloose.unam.exercise2.data.remote.dto.TeamSquadDto
import site.paoloose.unam.exercise2.data.repository.NoSquadFoundException
import site.paoloose.unam.exercise2.data.repository.SquadRepository

sealed class SquadUiState {
    object Loading : SquadUiState()
    data class Success(val squad: TeamSquadDto) : SquadUiState()
    data class Error(val message: String) : SquadUiState()
}

class SquadViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SquadRepository(ApiClient.worldCupApiService, ApiClient.footballApiService)
    private val _uiState = MutableStateFlow<SquadUiState>(SquadUiState.Loading)
    val uiState: StateFlow<SquadUiState> = _uiState.asStateFlow()

    fun fetchSquad(teamId: Int, useFootballApi: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = SquadUiState.Loading
            val result = repository.getSquad(teamId, useFootballApi)
            result.fold(
                onSuccess = { squad ->
                    _uiState.value = SquadUiState.Success(squad)
                },
                onFailure = { error ->
                    val message = when {
                        error is NoSquadFoundException ->
                            getApplication<Application>().getString(R.string.error_no_squad_found)
                        error.localizedMessage != null -> error.localizedMessage!!
                        else -> getApplication<Application>().getString(R.string.error_unknown)
                    }
                    _uiState.value = SquadUiState.Error(message)
                }
            )
        }
    }
}
