package com.alican.data.auth

import com.alican.data.mappers.toDomainModel
import com.alican.domain.models.User
import com.alican.domain.repository.UserAuthRepository
import com.alican.domain.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : UserAuthRepository {

    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun logout() = firebaseAuth.signOut()

    override suspend fun register(email: String, password: String): Resource<User> {
        if (!isValidEmail(email)) {
            return Resource.Error("Invalid email format")
        }

        if (!isValidPassword(password)) {
            return Resource.Error("Password must be at least 6 characters")
        }

        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                Resource.Success(
                    AuthUser(
                        uid = user.uid,
                        email = user.email,
                        isEmailVerified = user.isEmailVerified
                    ).toDomainModel()
                )
            } else {
                Resource.Error(message = "User creation failed")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Registration failed")
        }
    }

    override suspend fun login(email: String, password: String): Resource<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(
                message = e.message ?: "Login failed"
            )
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Resource<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Password reset failed")
        }
    }

    override suspend fun sendEmailVerification(): Resource<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                user.sendEmailVerification().await()
                Resource.Success(Unit)
            } else {
                Resource.Error("No user logged in")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Email verification failed")
        }
    }

    override fun getCurrentUser(): User? {
        val user = firebaseAuth.currentUser
        return user?.let {
            AuthUser(
                uid = it.uid,
                email = it.email,
                isEmailVerified = it.isEmailVerified
            ).toDomainModel()
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
