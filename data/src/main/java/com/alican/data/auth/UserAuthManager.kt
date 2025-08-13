package com.alican.data.auth

import com.alican.data.data.repository.UserAuthRepository
import com.alican.data.utils.ResultWrapper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : UserAuthRepository {

    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun logout() = firebaseAuth.signOut()

    override suspend fun register(email: String, password: String): ResultWrapper<AuthUser> {
        if (!isValidEmail(email)) {
            return ResultWrapper.Error("Invalid email format")
        }

        if (!isValidPassword(password)) {
            return ResultWrapper.Error("Password must be at least 6 characters")
        }

        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                ResultWrapper.Success(
                    AuthUser(
                        uid = user.uid,
                        email = user.email,
                        isEmailVerified = user.isEmailVerified
                    )
                )
            } else {
                ResultWrapper.Error(message = "User creation failed")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(message = e.message ?: "Registration failed")
        }
    }

    override suspend fun login(email: String, password: String): ResultWrapper<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(
                message = e.message ?: "Login failed"
            )
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): ResultWrapper<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Password reset failed")
        }
    }

    override suspend fun sendEmailVerification(): ResultWrapper<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                user.sendEmailVerification().await()
                ResultWrapper.Success(Unit)
            } else {
                ResultWrapper.Error("No user logged in")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Email verification failed")
        }
    }

    override fun getCurrentUser(): AuthUser? {
        val user = firebaseAuth.currentUser
        return user?.let {
            AuthUser(
                uid = it.uid,
                email = it.email,
                isEmailVerified = it.isEmailVerified
            )
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6 // Firebase minimum requirement
    }
}

data class AuthUser(
    val uid: String,
    val email: String?,
    val isEmailVerified: Boolean
)