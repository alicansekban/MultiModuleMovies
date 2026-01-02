package com.alican.multimodulemovies.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.UserAuthInteractor
import com.alican.domain.ui_models.BaseUIModel
import com.alican.multimodulemovies.helpers.navigation3.router.AppRouter
import com.alican.multimodulemovies.helpers.navigation3.router.navigateToAboutUs
import com.alican.multimodulemovies.helpers.navigation3.router.navigateToHelp
import com.alican.multimodulemovies.helpers.navigation3.router.navigateToLogin
import com.alican.multimodulemovies.helpers.navigation3.router.navigateToPrivacyPolicy
import com.alican.multimodulemovies.helpers.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userAuthInteractor: UserAuthInteractor,
    private val themeManager: ThemeManager,
    private val appRouter: AppRouter
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState: StateFlow<ProfileUIState> = _uiState.asStateFlow()

    init {
        observeTheme()
    }

    fun onScreenEvent(event: ProfileUIEvents) {
        when (event) {
            ProfileUIEvents.ClearError -> clearError()
            ProfileUIEvents.Logout -> logout()
            ProfileUIEvents.ToggleTheme -> toggleTheme()
            ProfileUIEvents.NavigateToLogin -> handleLogin()
            ProfileUIEvents.HandleNotifications -> handleNotifications()
            ProfileUIEvents.HandleHelp -> handleHelp()
            ProfileUIEvents.HandleAbout -> handleAbout()
            ProfileUIEvents.HandlePrivacyPolicy -> handlePrivacyPolicy()
            ProfileUIEvents.GetData -> loadUserData()
        }
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

    private fun toggleTheme() {
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

    private fun logout() {
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

    private fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }


    private fun handleLogin() {
        appRouter.navigateToLogin()
    }

    private fun handleNotifications() {
        // TODO: Implement notifications handling
    }

    private fun handleHelp() {
        appRouter.navigateToHelp()
    }

    private fun handleAbout() {
        appRouter.navigateToAboutUs()
    }

    private fun handlePrivacyPolicy() {
        appRouter.navigateToPrivacyPolicy()
    }
}