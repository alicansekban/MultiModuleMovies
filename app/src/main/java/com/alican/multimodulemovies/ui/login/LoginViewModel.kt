package com.alican.multimodulemovies.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.UserAuthInteractor
import com.alican.domain.ui_models.BaseUIModel
import com.alican.multimodulemovies.helpers.navigation3.AppRouter
import com.alican.multimodulemovies.helpers.navigation3.BottomNavRoutes
import com.alican.multimodulemovies.helpers.navigation3.navigateToRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userAuthInteractor: UserAuthInteractor,
    private val appRouter: AppRouter
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun onScreenEvent(event: LoginUIEvents) {
        when (event) {
            LoginUIEvents.ClearError -> clearError()
            LoginUIEvents.Login -> login()
            LoginUIEvents.NavigateToRegister -> navigateToRegister()
            LoginUIEvents.TogglePasswordVisibility -> togglePasswordVisibility()
            is LoginUIEvents.UpdateEmail -> updateEmail(event.email)
            is LoginUIEvents.UpdatePassword -> updatePassword(event.password)
        }
    }

    private fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    private fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    private fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    private fun login() {
        val currentState = _uiState.value

        if (!validateInputs(currentState)) return

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

            when (val result =
                userAuthInteractor.login(currentState.email, currentState.password)) {
                is BaseUIModel.Success -> {
                    _uiState.value = currentState.copy(isLoading = false)
                    navigateToHome()
                }

                is BaseUIModel.Error -> {
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }

                else -> {
                    _uiState.value = currentState.copy(isLoading = false)
                }
            }
        }
    }

    private fun validateInputs(state: LoginUIState): Boolean {
        when {
            state.email.isBlank() -> {
                _uiState.value = state.copy(errorMessage = "Email is required")
                return false
            }

            state.password.isBlank() -> {
                _uiState.value = state.copy(errorMessage = "Password is required")
                return false
            }
        }
        return true
    }

    private fun navigateToRegister() {
        appRouter.navigateToRegister()
    }

    private fun navigateToHome() {
        appRouter.navigateAndClearBackStack(BottomNavRoutes.Home)
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}