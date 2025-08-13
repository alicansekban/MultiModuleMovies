package com.alican.multimodulemovies.ui.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.UserAuthInteractor
import com.alican.domain.models.BaseUIModel
import com.alican.multimodulemovies.helpers.navigation.navigateToRegister
import com.alican.multimodulemovies.navigation.AppRouter
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

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    fun login() {
        val currentState = _uiState.value

        if (currentState.email.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Email is required")
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Password is required")
            return
        }

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

    fun navigateToRegister() {
        appRouter.navigateToRegister()
    }

    private fun navigateToHome() {
        appRouter.navigateAndClearBackStack(com.alican.multimodulemovies.utils.ScreenRoute.HomeHost)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}