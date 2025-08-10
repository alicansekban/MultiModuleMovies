package com.alican.multimodulemovies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.multimodulemovies.helpers.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getTheme()
    }

    private fun getTheme() {
        viewModelScope.launch {
            val isFirstTime = themeManager.isFirstTime()
            if (isFirstTime && themeManager.isSystemDark()) {
                _uiState.update {
                    it.copy(showThemeDialog = true)
                }
            }
            themeManager.observeTheme().collect { isDarkMode ->
                _uiState.update {
                    it.copy(isDarkMode = isDarkMode)
                }
            }
        }
    }

    fun setDarkModeFromDialog(isDarkMode: Boolean) {
        viewModelScope.launch {
            themeManager.setDarkMode(isDarkMode)
        }
        _uiState.update {
            it.copy(isDarkMode = isDarkMode, showThemeDialog = false)
        }
    }
}