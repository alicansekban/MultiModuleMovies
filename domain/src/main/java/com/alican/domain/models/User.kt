package com.alican.domain.models

data class User(
    val uid: String,
    val email: String?,
    val isEmailVerified: Boolean
)
