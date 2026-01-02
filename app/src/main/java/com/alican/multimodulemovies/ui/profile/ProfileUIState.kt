
package com.alican.multimodulemovies.ui.profile

import com.alican.domain.ui_models.user.UserAuthUIModel

data class ProfileUIState(
    val isUserLoggedIn: Boolean = false,
    val currentUser: UserAuthUIModel? = null,
    val userName: String = "Guest",
    val userSurname: String = "User",
    val userImageUrl: String? = null,
    val isDarkTheme: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface ProfileUIEvents {
    data object ToggleTheme : ProfileUIEvents
    data object Logout : ProfileUIEvents
    data object ClearError : ProfileUIEvents
    data object NavigateToLogin : ProfileUIEvents
    data object HandleNotifications : ProfileUIEvents
    data object HandleHelp : ProfileUIEvents
    data object HandleAbout : ProfileUIEvents
    data object HandlePrivacyPolicy : ProfileUIEvents
    data object GetData : ProfileUIEvents
}