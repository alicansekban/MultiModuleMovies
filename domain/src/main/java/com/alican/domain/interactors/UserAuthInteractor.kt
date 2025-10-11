package com.alican.domain.interactors

import com.alican.domain.repository.UserAuthRepository
import com.alican.domain.ui_models.BaseUIModel
import com.alican.domain.ui_models.BaseUIModel.Empty
import com.alican.domain.ui_models.BaseUIModel.Error
import com.alican.domain.ui_models.BaseUIModel.Loading
import com.alican.domain.ui_models.BaseUIModel.Success
import com.alican.domain.ui_models.user.UserAuthUIModel
import com.alican.domain.utils.Resource
import javax.inject.Inject

class UserAuthInteractor @Inject constructor(
    private val userAuthRepository: UserAuthRepository
) {

    fun isUserLoggedIn(): Boolean {
        return userAuthRepository.isUserLoggedIn()
    }

    fun getCurrentUser(): BaseUIModel<UserAuthUIModel> {
        return try {
            val authUser = userAuthRepository.getCurrentUser()
            if (authUser != null) {
                Success(
                    UserAuthUIModel(
                        uid = authUser.uid,
                        email = authUser.email,
                        isEmailVerified = authUser.isEmailVerified,
                        displayName = authUser.email?.substringBefore("@") ?: "User",
                        isLoggedIn = true
                    )
                )
            } else {
                Empty
            }
        } catch (e: Exception) {
            Error("Failed to get current user: ${e.message}")
        }
    }

    fun logout(): BaseUIModel<Unit> {
        return try {
            userAuthRepository.logout()
            Success(Unit)
        } catch (e: Exception) {
            Error("Logout failed: ${e.message}")
        }
    }

    suspend fun register(email: String, password: String): BaseUIModel<UserAuthUIModel> {
        return when (val result = userAuthRepository.register(email, password)) {
            is Resource.Success -> {
                Success(
                    UserAuthUIModel(
                        uid = result.value.uid,
                        email = result.value.email,
                        isEmailVerified = result.value.isEmailVerified,
                        displayName = result.value.email?.substringBefore("@") ?: "User",
                        isLoggedIn = true
                    )
                )
            }

            is Resource.Error -> {
                Error(result.message.orEmpty())
            }
        }
    }

    suspend fun login(email: String, password: String): BaseUIModel<UserAuthUIModel> {
        return when (val result = userAuthRepository.login(email, password)) {
            is Resource.Success -> {
                // After successful login, get the current user
                when (val currentUserResult = getCurrentUser()) {
                    is Success -> currentUserResult
                    is Error -> currentUserResult
                    Empty -> Error("Failed to retrieve user data after login")
                    Loading -> Error("Unexpected loading state")
                }
            }

            is Resource.Error -> {
                Error(result.message.orEmpty())
            }
        }
    }

    suspend fun sendPasswordResetEmail(email: String): BaseUIModel<String> {
        return when (val result = userAuthRepository.sendPasswordResetEmail(email)) {
            is Resource.Success -> {
                Success("Password reset email sent successfully")
            }

            is Resource.Error -> {
                Error(result.message.orEmpty())
            }
        }
    }

    suspend fun sendEmailVerification(): BaseUIModel<UserAuthUIModel> {
        return when (val result = userAuthRepository.sendEmailVerification()) {
            is Resource.Success -> {
                when (val currentUserResult = getCurrentUser()) {
                    is Success -> currentUserResult
                    is Error -> currentUserResult
                    Empty -> Error("No user logged in")
                    Loading -> Error("Unexpected loading state")
                }
            }

            is Resource.Error -> {
                Error(result.message.orEmpty())
            }
        }
    }
}