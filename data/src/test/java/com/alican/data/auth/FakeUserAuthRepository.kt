package com.alican.data.auth

import com.alican.domain.models.User
import com.alican.domain.repository.UserAuthRepository
import com.alican.domain.utils.Resource

class FakeUserAuthRepository : UserAuthRepository {
    private var currentUser: User? = null
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

    override suspend fun register(email: String, password: String): Resource<User> {
        if (shouldFailRegistration) {
            return Resource.Error(registrationException?.message ?: "Registration failed")
        }

        // Email validation (simplified)
        if (!email.contains("@")) {
            return Resource.Error("Invalid email format")
        }

        // Password validation
        if (password.length < 6) {
            return Resource.Error("Password must be at least 6 characters")
        }

        // Check if user already exists
        if (registeredUsers.containsKey(email)) {
            return Resource.Error("User already exists")
        }

        // Register user
        registeredUsers[email] = password
        val user = User(
            uid = "fake_uid_${email.hashCode()}",
            email = email,
            isEmailVerified = false
        )
        currentUser = user

        return Resource.Success(user)
    }

    override suspend fun login(email: String, password: String): Resource<Unit> {
        if (shouldFailLogin) {
            return Resource.Error(loginException?.message ?: "Login failed")
        }

        // Check if user exists and password matches
        val storedPassword = registeredUsers[email]
        if (storedPassword == null || storedPassword != password) {
            return Resource.Error("Invalid credentials")
        }

        // Login successful
        currentUser = User(
            uid = "fake_uid_${email.hashCode()}",
            email = email,
            isEmailVerified = false
        )

        return Resource.Success(Unit)
    }

    override suspend fun sendPasswordResetEmail(email: String): Resource<Unit> {
        if (shouldFailPasswordReset) {
            return Resource.Error("Password reset failed")
        }

        if (!registeredUsers.containsKey(email)) {
            return Resource.Error("User not found")
        }

        return Resource.Success(Unit)
    }

    override suspend fun sendEmailVerification(): Resource<Unit> {
        if (shouldFailEmailVerification) {
            return Resource.Error("Email verification failed")
        }

        if (currentUser == null) {
            return Resource.Error("No user logged in")
        }

        // Simulate email verification sent
        currentUser = currentUser?.copy(isEmailVerified = true)

        return Resource.Success(Unit)
    }

    override fun getCurrentUser(): User? = currentUser

    // Test helper methods
    fun setCurrentUser(user: User?) {
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