package com.alican.multimodulemovies.ui.profile

import com.alican.domain.models.UserAuthUIModel


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
