package com.alican.data.data.repository

import com.alican.data.auth.AuthUser
import com.alican.data.utils.ResultWrapper

interface UserAuthRepository {
    fun isUserLoggedIn(): Boolean
    fun logout()
    suspend fun register(email: String, password: String): ResultWrapper<AuthUser>
    suspend fun login(email: String, password: String): ResultWrapper<Unit>
    suspend fun sendPasswordResetEmail(email: String): ResultWrapper<Unit>
    suspend fun sendEmailVerification(): ResultWrapper<Unit>
    fun getCurrentUser(): AuthUser?
}
