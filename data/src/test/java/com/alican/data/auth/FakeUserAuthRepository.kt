package com.alican.data.auth

import com.alican.data.data.repository.UserAuthRepository
import com.alican.data.utils.ResultWrapper

class FakeUserAuthRepository : UserAuthRepository {
    private var currentUser: AuthUser? = null
    private var shouldFailLogin = false
    private var shouldFailRegistration = false
    private var shouldFailPasswordReset = false
    private var shouldFailEmailVerification = false
    private var loginException: Exception? = null
    private var registrationException: Exception? = null

    // Test data
    private val registeredUsers = mutableMapOf<String, String>() // email -> password

    override fun isUserLoggedIn(): Boolean = currentUser != null

    override fun logout() {
        currentUser = null
    }

    override suspend fun register(email: String, password: String): ResultWrapper<AuthUser> {
        if (shouldFailRegistration) {
            return ResultWrapper.Error(registrationException?.message ?: "Registration failed")
        }

        // Email validation (simplified)
        if (!email.contains("@")) {
            return ResultWrapper.Error("Invalid email format")
        }

        // Password validation
        if (password.length < 6) {
            return ResultWrapper.Error("Password must be at least 6 characters")
        }

        // Check if user already exists
        if (registeredUsers.containsKey(email)) {
            return ResultWrapper.Error("User already exists")
        }

        // Register user
        registeredUsers[email] = password
        val user = AuthUser(
            uid = "fake_uid_${email.hashCode()}",
            email = email,
            isEmailVerified = false
        )
        currentUser = user

        return ResultWrapper.Success(user)
    }

    override suspend fun login(email: String, password: String): ResultWrapper<Unit> {
        if (shouldFailLogin) {
            return ResultWrapper.Error(loginException?.message ?: "Login failed")
        }

        // Check if user exists and password matches
        val storedPassword = registeredUsers[email]
        if (storedPassword == null || storedPassword != password) {
            return ResultWrapper.Error("Invalid credentials")
        }

        // Login successful
        currentUser = AuthUser(
            uid = "fake_uid_${email.hashCode()}",
            email = email,
            isEmailVerified = false
        )

        return ResultWrapper.Success(Unit)
    }

    override suspend fun sendPasswordResetEmail(email: String): ResultWrapper<Unit> {
        if (shouldFailPasswordReset) {
            return ResultWrapper.Error("Password reset failed")
        }

        if (!registeredUsers.containsKey(email)) {
            return ResultWrapper.Error("User not found")
        }

        return ResultWrapper.Success(Unit)
    }

    override suspend fun sendEmailVerification(): ResultWrapper<Unit> {
        if (shouldFailEmailVerification) {
            return ResultWrapper.Error("Email verification failed")
        }

        if (currentUser == null) {
            return ResultWrapper.Error("No user logged in")
        }

        // Simulate email verification sent
        currentUser = currentUser?.copy(isEmailVerified = true)

        return ResultWrapper.Success(Unit)
    }

    override fun getCurrentUser(): AuthUser? = currentUser

    // Test helper methods
    fun setCurrentUser(user: AuthUser?) {
        currentUser = user
    }

    fun setShouldFailLogin(shouldFail: Boolean, exception: Exception? = null) {
        shouldFailLogin = shouldFail
        loginException = exception
    }

    fun setShouldFailRegistration(shouldFail: Boolean, exception: Exception? = null) {
        shouldFailRegistration = shouldFail
        registrationException = exception
    }

    fun setShouldFailPasswordReset(shouldFail: Boolean) {
        shouldFailPasswordReset = shouldFail
    }

    fun setShouldFailEmailVerification(shouldFail: Boolean) {
        shouldFailEmailVerification = shouldFail
    }

    fun addRegisteredUser(email: String, password: String) {
        registeredUsers[email] = password
    }

    fun clearRegisteredUsers() {
        registeredUsers.clear()
    }

    fun getRegisteredUsers(): Map<String, String> = registeredUsers.toMap()
}