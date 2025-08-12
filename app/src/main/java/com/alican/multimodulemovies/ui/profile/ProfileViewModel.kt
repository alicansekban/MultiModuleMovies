package com.alican.multimodulemovies.ui.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.UserAuthInteractor
import com.alican.domain.models.BaseUIModel
import com.alican.multimodulemovies.helpers.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userAuthInteractor: UserAuthInteractor,
    private val themeManager: ThemeManager
) : ViewModel() {

    private val _uiState = mutableStateOf(ProfileUIState())
    val uiState: State<ProfileUIState> = _uiState

    init {
        loadUserData()
        observeTheme()
    }

    private fun loadUserData() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        try {
            val isLoggedIn = userAuthInteractor.isUserLoggedIn()

            when (val currentUserResult = userAuthInteractor.getCurrentUser()) {
                is BaseUIModel.Success -> {
                    when (val displayInfoResult = userAuthInteractor.getCurrentUser()) {
                        is BaseUIModel.Success -> {
                            val (userName, userSurname) = displayInfoResult.data
                            _uiState.value = _uiState.value.copy(
                                isUserLoggedIn = isLoggedIn,
                                currentUser = currentUserResult.data,
                                userName = userName,
                                userSurname = userSurname.orEmpty(),
                                isLoading = false,
                                error = null
                            )
                        }

                        is BaseUIModel.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = displayInfoResult.message
                            )
                        }

                        BaseUIModel.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true)
                        }

                        BaseUIModel.Empty -> {
                            _uiState.value = _uiState.value.copy(
                                isUserLoggedIn = false,
                                currentUser = null,
                                userName = "Guest",
                                userSurname = "User",
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }

                is BaseUIModel.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = currentUserResult.message
                    )
                }

                BaseUIModel.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }

                BaseUIModel.Empty -> {
                    _uiState.value = _uiState.value.copy(
                        isUserLoggedIn = false,
                        currentUser = null,
                        userName = "Guest",
                        userSurname = "User",
                        isLoading = false,
                        error = null
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Failed to load user data: ${e.message}"
            )
        }
    }

    private fun observeTheme() {
        viewModelScope.launch {
            try {
                themeManager.observeTheme().collect { isDark ->
                    _uiState.value = _uiState.value.copy(isDarkTheme = isDark)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load theme: ${e.message}"
                )
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            try {
                val newTheme = !_uiState.value.isDarkTheme
                themeManager.setDarkMode(newTheme)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to toggle theme: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                when (val logoutResult = userAuthInteractor.logout()) {
                    is BaseUIModel.Success -> {
                        loadUserData()
                    }

                    is BaseUIModel.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = logoutResult.message
                        )
                    }

                    BaseUIModel.Loading -> {
                        // Keep loading state
                    }

                    BaseUIModel.Empty -> {
                        loadUserData()
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to logout: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}