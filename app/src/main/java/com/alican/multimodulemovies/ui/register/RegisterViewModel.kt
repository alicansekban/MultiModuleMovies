package com.alican.multimodulemovies.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.UserAuthInteractor
import com.alican.domain.models.BaseUIModel
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

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword, errorMessage = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible
        )
    }

    fun register() {
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

    fun navigateToLogin() {
        appRouter.navigateBack()
    }

    private fun navigateToHome() {
        appRouter.navigateAndClearBackStack(ScreenRoute.HomeHost)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}