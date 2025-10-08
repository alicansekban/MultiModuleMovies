package com.alican.domain.ui_models.user

data class UserAuthUIModel(
    val uid: String,
    val email: String?,
    val isEmailVerified: Boolean,
    val displayName: String,
    val isLoggedIn: Boolean
)