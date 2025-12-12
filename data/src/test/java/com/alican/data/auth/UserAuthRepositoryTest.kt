package com.alican.data.auth

import com.alican.data.utils.ResultWrapper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserAuthRepositoryTest {

    private lateinit var fakeRepository: FakeUserAuthRepository

    @Before
    fun setup() {
        fakeRepository = FakeUserAuthRepository()
    }

    @Test
    fun `isUserLoggedIn returns false initially`() {
        // When
        val result = fakeRepository.isUserLoggedIn()

        // Then
        assertFalse(result)
    }

    @Test
    fun `isUserLoggedIn returns true after setting current user`() {
        // Given
        val user = AuthUser("uid", "test@example.com", false)
        fakeRepository.setCurrentUser(user)

        // When
        val result = fakeRepository.isUserLoggedIn()

        // Then
        assertTrue(result)
    }

    @Test
    fun `logout clears current user`() {
        // Given
        val user = AuthUser("uid", "test@example.com", false)
        fakeRepository.setCurrentUser(user)
        assertTrue(fakeRepository.isUserLoggedIn())

        // When
        fakeRepository.logout()

        // Then
        assertFalse(fakeRepository.isUserLoggedIn())
        assertNull(fakeRepository.getCurrentUser())
    }

    @Test
    fun `getCurrentUser returns null when no user is logged in`() {
        // When
        val result = fakeRepository.getCurrentUser()

        // Then
        assertNull(result)
    }

    @Test
    fun `getCurrentUser returns current user when logged in`() {
        // Given
        val user = AuthUser("uid", "test@example.com", true)
        fakeRepository.setCurrentUser(user)

        // When
        val result = fakeRepository.getCurrentUser()

        // Then
        assertEquals(user, result)
    }

    @Test
    fun `register with valid credentials returns success`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "password123"

        // When
        val result = fakeRepository.register(email, password)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val authUser = (result as ResultWrapper.Success).value
        assertEquals(email, authUser.email)
        assertNotNull(authUser.uid)
        assertFalse(authUser.isEmailVerified)
        assertTrue(fakeRepository.isUserLoggedIn())
    }

    @Test
    fun `register with invalid email returns error`() = runBlocking {
        // Given
        val invalidEmail = "invalid-email"
        val password = "password123"

        // When
        val result = fakeRepository.register(invalidEmail, password)

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals("Invalid email format", (result as ResultWrapper.Error).message)
        assertFalse(fakeRepository.isUserLoggedIn())
    }

    @Test
    fun `register with short password returns error`() = runBlocking {
        // Given
        val email = "test@example.com"
        val shortPassword = "12345"

        // When
        val result = fakeRepository.register(email, shortPassword)

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals(
            "Password must be at least 6 characters",
            (result as ResultWrapper.Error).message
        )
        assertFalse(fakeRepository.isUserLoggedIn())
    }

    @Test
    fun `register with existing email returns error`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "password123"
        fakeRepository.addRegisteredUser(email, password)

        // When
        val result = fakeRepository.register(email, "newpassword123")

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals("User already exists", (result as ResultWrapper.Error).message)
        assertFalse(fakeRepository.isUserLoggedIn())
    }

    @Test
    fun `register returns error when failure is configured`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "password123"
        fakeRepository.setShouldFailRegistration(true, Exception("Registration error"))

        // When
        val result = fakeRepository.register(email, password)

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals("Registration error", (result as ResultWrapper.Error).message)
    }

    @Test
    fun `login with valid credentials returns success`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "password123"
        fakeRepository.addRegisteredUser(email, password)

        // When
        val result = fakeRepository.login(email, password)

        // Then
        assertTrue(result is ResultWrapper.Success)
        assertTrue(fakeRepository.isUserLoggedIn())
        assertEquals(email, fakeRepository.getCurrentUser()?.email)
    }

    @Test
    fun `login with invalid credentials returns error`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "password123"
        fakeRepository.addRegisteredUser(email, password)

        // When
        val result = fakeRepository.login(email, "wrongpassword")

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals("Invalid credentials", (result as ResultWrapper.Error).message)
        assertFalse(fakeRepository.isUserLoggedIn())
    }

    @Test
    fun `login with non-existent user returns error`() = runBlocking {
        // Given
        val email = "nonexistent@example.com"
        val password = "password123"

        // When
        val result = fakeRepository.login(email, password)

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals("Invalid credentials", (result as ResultWrapper.Error).message)
        assertFalse(fakeRepository.isUserLoggedIn())
    }

    @Test
    fun `login returns error when failure is configured`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "password123"
        fakeRepository.addRegisteredUser(email, password)
        fakeRepository.setShouldFailLogin(true, Exception("Network error"))

        // When
        val result = fakeRepository.login(email, password)

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals("Network error", (result as ResultWrapper.Error).message)
        assertFalse(fakeRepository.isUserLoggedIn())
    }

    @Test
    fun `sendPasswordResetEmail with existing user returns success`() = runBlocking {
        // Given
        val email = "test@example.com"
        fakeRepository.addRegisteredUser(email, "password123")

        // When
        val result = fakeRepository.sendPasswordResetEmail(email)

        // Then
        assertTrue(result is ResultWrapper.Success)
    }

    @Test
    fun `sendPasswordResetEmail with non-existent user returns error`() = runBlocking {
        // Given
        val email = "nonexistent@example.com"

        // When
        val result = fakeRepository.sendPasswordResetEmail(email)

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals("User not found", (result as ResultWrapper.Error).message)
    }

    @Test
    fun `sendPasswordResetEmail returns error when failure is configured`() = runBlocking {
        // Given
        val email = "test@example.com"
        fakeRepository.addRegisteredUser(email, "password123")
        fakeRepository.setShouldFailPasswordReset(true)

        // When
        val result = fakeRepository.sendPasswordResetEmail(email)

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals("Password reset failed", (result as ResultWrapper.Error).message)
    }

    @Test
    fun `sendEmailVerification with logged in user returns success`() = runBlocking {
        // Given
        val user = AuthUser("uid", "test@example.com", false)
        fakeRepository.setCurrentUser(user)

        // When
        val result = fakeRepository.sendEmailVerification()

        // Then
        assertTrue(result is ResultWrapper.Success)
        assertTrue(fakeRepository.getCurrentUser()?.isEmailVerified == true)
    }

    @Test
    fun `sendEmailVerification with no logged in user returns error`() = runBlocking {
        // When
        val result = fakeRepository.sendEmailVerification()

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals("No user logged in", (result as ResultWrapper.Error).message)
    }

    @Test
    fun `sendEmailVerification returns error when failure is configured`() = runBlocking {
        // Given
        val user = AuthUser("uid", "test@example.com", false)
        fakeRepository.setCurrentUser(user)
        fakeRepository.setShouldFailEmailVerification(true)

        // When
        val result = fakeRepository.sendEmailVerification()

        // Then
        assertTrue(result is ResultWrapper.Error)
        assertEquals("Email verification failed", (result as ResultWrapper.Error).message)
    }

    @Test
    fun `all methods return error when configured to fail`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val user = AuthUser("uid", email, false)

        fakeRepository.addRegisteredUser(email, password)
        fakeRepository.setCurrentUser(user)
        fakeRepository.setShouldFailLogin(true, Exception("Login failed"))
        fakeRepository.setShouldFailRegistration(true, Exception("Registration failed"))
        fakeRepository.setShouldFailPasswordReset(true)
        fakeRepository.setShouldFailEmailVerification(true)

        // When & Then
        val loginResult = fakeRepository.login(email, password)
        val registerResult = fakeRepository.register("new@example.com", password)
        val passwordResetResult = fakeRepository.sendPasswordResetEmail(email)
        val emailVerificationResult = fakeRepository.sendEmailVerification()

        // All should return errors
        listOf(
            loginResult,
            registerResult,
            passwordResetResult,
            emailVerificationResult
        ).forEach { result ->
            assertTrue(result is ResultWrapper.Error)
        }

        assertEquals("Login failed", (loginResult as ResultWrapper.Error).message)
        assertEquals("Registration failed", (registerResult as ResultWrapper.Error).message)
        assertEquals("Password reset failed", (passwordResetResult as ResultWrapper.Error).message)
        assertEquals(
            "Email verification failed",
            (emailVerificationResult as ResultWrapper.Error).message
        )
    }

    @Test
    fun `complete user authentication flow`() = runBlocking {
        // Register new user
        val email = "flow@test.com"
        val password = "testpassword123"
        val registerResult = fakeRepository.register(email, password)

        assertTrue(registerResult is ResultWrapper.Success)
        assertTrue(fakeRepository.isUserLoggedIn())
        assertEquals(email, fakeRepository.getCurrentUser()?.email)
        assertFalse(fakeRepository.getCurrentUser()?.isEmailVerified == true)

        // Send email verification
        val verificationResult = fakeRepository.sendEmailVerification()
        assertTrue(verificationResult is ResultWrapper.Success)
        assertTrue(fakeRepository.getCurrentUser()?.isEmailVerified == true)

        // Logout
        fakeRepository.logout()
        assertFalse(fakeRepository.isUserLoggedIn())

        // Login again
        val loginResult = fakeRepository.login(email, password)
        assertTrue(loginResult is ResultWrapper.Success)
        assertTrue(fakeRepository.isUserLoggedIn())

        // Test password reset
        fakeRepository.logout()
        val passwordResetResult = fakeRepository.sendPasswordResetEmail(email)
        assertTrue(passwordResetResult is ResultWrapper.Success)
    }

    @Test
    fun `helper methods work correctly`() {
        // Test setCurrentUser
        val user = AuthUser("test_uid", "helper@test.com", true)
        fakeRepository.setCurrentUser(user)
        assertEquals(user, fakeRepository.getCurrentUser())

        // Test addRegisteredUser and getRegisteredUsers
        fakeRepository.addRegisteredUser("test1@example.com", "pass1")
        fakeRepository.addRegisteredUser("test2@example.com", "pass2")

        val registeredUsers = fakeRepository.getRegisteredUsers()
        assertEquals(2, registeredUsers.size)
        assertEquals("pass1", registeredUsers["test1@example.com"])
        assertEquals("pass2", registeredUsers["test2@example.com"])

        // Test clearRegisteredUsers
        fakeRepository.clearRegisteredUsers()
        assertTrue(fakeRepository.getRegisteredUsers().isEmpty())
    }
}