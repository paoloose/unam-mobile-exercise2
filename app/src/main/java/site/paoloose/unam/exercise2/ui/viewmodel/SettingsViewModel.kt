package site.paoloose.unam.exercise2.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode { GREEN, BLUE, RED, VIOLET }
enum class ApiSource { COMPUTOMOVIL_MOCK, API_FOOTBALL }

class SettingsViewModel : ViewModel() {
    private val _themeMode = MutableStateFlow(ThemeMode.GREEN)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _apiSource = MutableStateFlow(ApiSource.COMPUTOMOVIL_MOCK)
    val apiSource: StateFlow<ApiSource> = _apiSource.asStateFlow()

    fun setThemeMode(mode: ThemeMode) { _themeMode.value = mode }
    fun setApiSource(source: ApiSource) { _apiSource.value = source }
}
