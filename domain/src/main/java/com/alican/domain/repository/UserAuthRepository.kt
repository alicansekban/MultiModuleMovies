package com.alican.domain.repository

import com.alican.domain.models.User
import com.alican.domain.utils.Resource

interface UserAuthRepository {
    fun isUserLoggedIn(): Boolean
    fun logout()
    suspend fun register(email: String, password: String): Resource<User>
    suspend fun login(email: String, password: String): Resource<Unit>
    suspend fun sendPasswordResetEmail(email: String): Resource<Unit>
    suspend fun sendEmailVerification(): Resource<Unit>
    fun getCurrentUser(): User?
}
