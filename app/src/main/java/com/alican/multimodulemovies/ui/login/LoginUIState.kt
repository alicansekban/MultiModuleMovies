package com.alican.multimodulemovies.ui.login

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface LoginUIEvents {
    data object Login : LoginUIEvents
    data object ClearError : LoginUIEvents
    data object TogglePasswordVisibility : LoginUIEvents
    data object NavigateToRegister : LoginUIEvents
    data class UpdateEmail(val email: String) : LoginUIEvents
    data class UpdatePassword(val password: String) : LoginUIEvents
}