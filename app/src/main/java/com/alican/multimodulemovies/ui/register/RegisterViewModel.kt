
package com.alican.multimodulemovies.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.UserAuthInteractor
import com.alican.domain.ui_models.BaseUIModel
import com.alican.multimodulemovies.navigation.AppRouter
import com.alican.multimodulemovies.utils.ScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userAuthInteractor: UserAuthInteractor,
    private val appRouter: AppRouter
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun onScreenEvent(event: RegisterUIEvents) {
        when (event) {
            RegisterUIEvents.ClearError -> clearError()
            RegisterUIEvents.ClearSuccess -> clearSuccess()
            RegisterUIEvents.Register -> register()
            RegisterUIEvents.ToggleConfirmPasswordVisibility -> toggleConfirmPasswordVisibility()
            RegisterUIEvents.TogglePasswordVisibility -> togglePasswordVisibility()
            RegisterUIEvents.NavigateToLogin -> navigateToLogin()
            is RegisterUIEvents.UpdateConfirmPassword -> updateConfirmPassword(event.confirmPassword)
            is RegisterUIEvents.UpdateEmail -> updateEmail(event.email)
            is RegisterUIEvents.UpdatePassword -> updatePassword(event.password)
        }
    }

    private fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    private fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword, errorMessage = null)
    }

    private fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    private fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible
        )
    }

    private fun register() {
        val currentState = _uiState.value

        if (!validateInputs(currentState)) return

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

            when (val result =
                userAuthInteractor.register(currentState.email, currentState.password)) {
                is BaseUIModel.Success -> {
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        registrationSuccess = true
                    )
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

    private fun validateInputs(state: RegisterUIState): Boolean {
        when {
            state.email.isBlank() -> {
                _uiState.value = state.copy(errorMessage = "Email is required")
                return false
            }

            state.password.isBlank() -> {
                _uiState.value = state.copy(errorMessage = "Password is required")
                return false
            }

            state.password.length < 6 -> {
                _uiState.value = state.copy(errorMessage = "Password must be at least 6 characters")
                return false
            }

            state.confirmPassword.isBlank() -> {
                _uiState.value = state.copy(errorMessage = "Please confirm your password")
                return false
            }

            state.password != state.confirmPassword -> {
                _uiState.value = state.copy(errorMessage = "Passwords do not match")
                return false
            }
        }
        return true
    }

    private fun navigateToLogin() {
        appRouter.navigateBack()
    }

    private fun navigateToHome() {
        appRouter.navigateAndClearBackStack(ScreenRoute.HomeHost)
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun clearSuccess() {
        _uiState.value = _uiState.value.copy(registrationSuccess = false)
    }
}