package site.paoloose.unam.exercise2.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// We support both the mock teacher url and the official football api

enum class ApiSource { COMPUTOMOVIL_MOCK, API_FOOTBALL }

class SettingsViewModel : ViewModel() {
    private val _apiSource = MutableStateFlow(ApiSource.COMPUTOMOVIL_MOCK)
    val apiSource: StateFlow<ApiSource> = _apiSource.asStateFlow()

    fun setApiSource(source: ApiSource) { _apiSource.value = source }
}
