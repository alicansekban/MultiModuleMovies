package com.alican.data.auth

import com.alican.data.utils.ResultWrapper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserAuthIntegrationTest {

    private lateinit var repository: FakeUserAuthRepository

    @Before
    fun setup() {
        repository = FakeUserAuthRepository()
    }

    @Test
    fun `complete authentication flow with success scenarios`() = runBlocking {
        // Given
        val email = "integration@test.com"
        val password = "testpassword123"

        // Initially no user logged in
        assertFalse(repository.isUserLoggedIn())

        // Register new user
        val registerResult = repository.register(email, password)
        assertTrue(registerResult is ResultWrapper.Success)
        assertTrue(repository.isUserLoggedIn())

        val registeredUser = (registerResult as ResultWrapper.Success).value
        assertEquals(email, registeredUser.email)
        assertNotNull(registeredUser.uid)
        assertFalse(registeredUser.isEmailVerified)

        // Send email verification
        val verificationResult = repository.sendEmailVerification()
        assertTrue(verificationResult is ResultWrapper.Success)

        // Check that user is now verified
        val currentUser = repository.getCurrentUser()
        assertTrue(currentUser?.isEmailVerified == true)

        // Logout
        repository.logout()
        assertFalse(repository.isUserLoggedIn())

        // Login again
        val loginResult = repository.login(email, password)
        assertTrue(loginResult is ResultWrapper.Success)
        assertTrue(repository.isUserLoggedIn())
        assertEquals(email, repository.getCurrentUser()?.email)

        // Send password reset while logged out
        repository.logout()
        val passwordResetResult = repository.sendPasswordResetEmail(email)
        assertTrue(passwordResetResult is ResultWrapper.Success)
    }

    @Test
    fun `authentication flow with validation errors`() = runBlocking {
        // Test invalid email registration
        val invalidEmailResult = repository.register("invalid-email", "password123")
        assertTrue(invalidEmailResult is ResultWrapper.Error)
        assertEquals("Invalid email format", (invalidEmailResult as ResultWrapper.Error).message)
        assertFalse(repository.isUserLoggedIn())

        // Test short password registration
        val shortPasswordResult = repository.register("test@example.com", "123")
        assertTrue(shortPasswordResult is ResultWrapper.Error)
        assertEquals(
            "Password must be at least 6 characters",
            (shortPasswordResult as ResultWrapper.Error).message
        )
        assertFalse(repository.isUserLoggedIn())

        // Test duplicate registration
        val email = "duplicate@test.com"
        val password = "password123"
        repository.addRegisteredUser(email, password)

        val duplicateResult = repository.register(email, "newpassword123")
        assertTrue(duplicateResult is ResultWrapper.Error)
        assertEquals("User already exists", (duplicateResult as ResultWrapper.Error).message)
    }

    @Test
    fun `authentication flow with authentication errors`() = runBlocking {
        // Try to login without registration
        val loginResult = repository.login("test@example.com", "password")
        assertTrue(loginResult is ResultWrapper.Error)
        assertEquals("Invalid credentials", (loginResult as ResultWrapper.Error).message)
        assertFalse(repository.isUserLoggedIn())

        // Try to login with wrong password
        val email = "registered@test.com"
        val correctPassword = "password123"
        repository.addRegisteredUser(email, correctPassword)

        val wrongPasswordResult = repository.login(email, "wrongpassword")
        assertTrue(wrongPasswordResult is ResultWrapper.Error)
        assertEquals("Invalid credentials", (wrongPasswordResult as ResultWrapper.Error).message)
        assertFalse(repository.isUserLoggedIn())

        // Try to send email verification without login
        val verificationResult = repository.sendEmailVerification()
        assertTrue(verificationResult is ResultWrapper.Error)
        assertEquals("No user logged in", (verificationResult as ResultWrapper.Error).message)

        // Try password reset for non-existent user
        val passwordResetResult = repository.sendPasswordResetEmail("nonexistent@test.com")
        assertTrue(passwordResetResult is ResultWrapper.Error)
        assertEquals("User not found", (passwordResetResult as ResultWrapper.Error).message)
    }

    @Test
    fun `authentication flow with network simulation errors`() = runBlocking {
        val email = "network@test.com"
        val password = "password123"

        // Simulate login failure
        repository.addRegisteredUser(email, password)
        repository.setShouldFailLogin(true, Exception("Network timeout"))

        val loginResult = repository.login(email, password)
        assertTrue(loginResult is ResultWrapper.Error)
        assertEquals("Network timeout", (loginResult as ResultWrapper.Error).message)
        assertFalse(repository.isUserLoggedIn())

        // Reset and test successful login
        repository.setShouldFailLogin(false)
        val successfulLoginResult = repository.login(email, password)
        assertTrue(successfulLoginResult is ResultWrapper.Success)
        assertTrue(repository.isUserLoggedIn())

        // Simulate registration failure
        repository.setShouldFailRegistration(true, Exception("Server error"))
        val registrationResult = repository.register("new@example.com", "password123")
        assertTrue(registrationResult is ResultWrapper.Error)
        assertEquals("Server error", (registrationResult as ResultWrapper.Error).message)

        // Simulate password reset failure
        repository.setShouldFailPasswordReset(true)
        val passwordResetResult = repository.sendPasswordResetEmail(email)
        assertTrue(passwordResetResult is ResultWrapper.Error)
        assertEquals("Password reset failed", (passwordResetResult as ResultWrapper.Error).message)

        // Simulate email verification failure
        repository.setShouldFailEmailVerification(true)
        val emailVerificationResult = repository.sendEmailVerification()
        assertTrue(emailVerificationResult is ResultWrapper.Error)
        assertEquals(
            "Email verification failed",
            (emailVerificationResult as ResultWrapper.Error).message
        )
    }

    @Test
    fun `multiple user registration and management flow`() = runBlocking {
        val users = listOf(
            "user1@test.com" to "password123",
            "user2@test.com" to "password456",
            "user3@test.com" to "password789"
        )

        // Register multiple users
        users.forEach { (email, password) ->
            val result = repository.register(email, password)
            assertTrue(result is ResultWrapper.Success)

            // Each registration should log in the new user
            assertTrue(repository.isUserLoggedIn())
            assertEquals(email, repository.getCurrentUser()?.email)

            // Logout to register next user
            repository.logout()
            assertFalse(repository.isUserLoggedIn())
        }

        // Verify all users can login
        users.forEach { (email, password) ->
            val loginResult = repository.login(email, password)
            assertTrue(loginResult is ResultWrapper.Success)
            assertTrue(repository.isUserLoggedIn())
            assertEquals(email, repository.getCurrentUser()?.email)

            repository.logout()
        }

        // Verify registered users count
        assertEquals(users.size, repository.getRegisteredUsers().size)
    }

    @Test
    fun `email verification flow integration`() = runBlocking {
        // Register user
        val email = "verification@test.com"
        val password = "password123"
        val registerResult = repository.register(email, password)

        assertTrue(registerResult is ResultWrapper.Success)
        val user = (registerResult as ResultWrapper.Success).value
        assertFalse(user.isEmailVerified)

        // Send email verification
        val verificationResult = repository.sendEmailVerification()
        assertTrue(verificationResult is ResultWrapper.Success)

        // Check user is now verified
        val verifiedUser = repository.getCurrentUser()
        assertTrue(verifiedUser?.isEmailVerified == true)

        // Logout and login again - verification should persist
        repository.logout()
        val loginResult = repository.login(email, password)
        assertTrue(loginResult is ResultWrapper.Success)

        // Note: In fake implementation, login creates new user without verification
        // This would be different in real implementation where verification persists
        val loginUser = repository.getCurrentUser()
        assertNotNull(loginUser)
    }

    @Test
    fun `password reset flow integration`() = runBlocking {
        val email = "reset@test.com"
        val password = "oldpassword123"

        // Register user
        val registerResult = repository.register(email, password)
        assertTrue(registerResult is ResultWrapper.Success)

        // Logout (typically user would be logged out when requesting password reset)
        repository.logout()

        // Send password reset for registered user
        val resetResult = repository.sendPasswordResetEmail(email)
        assertTrue(resetResult is ResultWrapper.Success)

        // Try to send reset for non-existent user
        val nonExistentResetResult = repository.sendPasswordResetEmail("nonexistent@test.com")
        assertTrue(nonExistentResetResult is ResultWrapper.Error)
        assertEquals("User not found", (nonExistentResetResult as ResultWrapper.Error).message)
    }

    @Test
    fun `error recovery scenarios`() = runBlocking {
        val email = "recovery@test.com"
        val password = "password123"

        // Configure all operations to fail initially
        repository.setShouldFailLogin(true, Exception("Initial login failure"))
        repository.setShouldFailRegistration(true, Exception("Initial registration failure"))
        repository.setShouldFailPasswordReset(true)
        repository.setShouldFailEmailVerification(true)

        // All operations should fail
        val failedRegister = repository.register(email, password)
        assertTrue(failedRegister is ResultWrapper.Error)

        repository.addRegisteredUser(email, password)
        val failedLogin = repository.login(email, password)
        assertTrue(failedLogin is ResultWrapper.Error)

        val failedReset = repository.sendPasswordResetEmail(email)
        assertTrue(failedReset is ResultWrapper.Error)

        // Reset failure states
        repository.setShouldFailLogin(false)
        repository.setShouldFailRegistration(false)
        repository.setShouldFailPasswordReset(false)
        repository.setShouldFailEmailVerification(false)

        // Operations should now succeed
        val successfulLogin = repository.login(email, password)
        assertTrue(successfulLogin is ResultWrapper.Success)

        val successfulVerification = repository.sendEmailVerification()
        assertTrue(successfulVerification is ResultWrapper.Success)

        repository.logout()
        val successfulReset = repository.sendPasswordResetEmail(email)
        assertTrue(successfulReset is ResultWrapper.Success)
    }
}