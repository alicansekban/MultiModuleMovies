package com.alican.domain.models


data class UserAuthUIModel(
    val uid: String,
    val email: String?,
    val isEmailVerified: Boolean,
    val displayName: String,
    val isLoggedIn: Boolean
)