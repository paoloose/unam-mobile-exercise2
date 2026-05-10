package site.paoloose.unam.exercise2.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ApiSource { COMPUTOMOVIL_MOCK, API_FOOTBALL }
enum class ThemeMode { DARK, LIGHT }

class SettingsViewModel : ViewModel() {
    private val _apiSource = MutableStateFlow(ApiSource.API_FOOTBALL)
    val apiSource: StateFlow<ApiSource> = _apiSource.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.DARK)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun setApiSource(source: ApiSource) { _apiSource.value = source }
    fun setThemeMode(mode: ThemeMode) { _themeMode.value = mode }
}
