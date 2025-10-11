package com.alican.data.mappers

import com.alican.data.auth.AuthUser
import com.alican.domain.models.User

fun AuthUser.toDomainModel(): User {
    return User(
        uid = uid,
        email = email,
        isEmailVerified = isEmailVerified
    )
}
