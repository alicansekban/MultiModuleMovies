package com.alican.multimodulemovies.ui.register

data class RegisterUIState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val registrationSuccess: Boolean = false
)

sealed interface RegisterUIEvents {
    data object Register : RegisterUIEvents
    data object ClearError : RegisterUIEvents
    data object ClearSuccess : RegisterUIEvents
    data object TogglePasswordVisibility : RegisterUIEvents
    data object ToggleConfirmPasswordVisibility : RegisterUIEvents
    data object NavigateToLogin : RegisterUIEvents
    data class UpdateEmail(val email: String) : RegisterUIEvents
    data class UpdatePassword(val password: String) : RegisterUIEvents
    data class UpdateConfirmPassword(val confirmPassword: String) : RegisterUIEvents
}