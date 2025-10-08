package com.alican.domain.interactors

import com.alican.data.auth.UserAuthManager
import com.alican.data.utils.ResultWrapper
import com.alican.domain.ui_models.BaseUIModel
import com.alican.domain.ui_models.BaseUIModel.Empty
import com.alican.domain.ui_models.BaseUIModel.Error
import com.alican.domain.ui_models.BaseUIModel.Loading
import com.alican.domain.ui_models.BaseUIModel.Success
import com.alican.domain.ui_models.user.UserAuthUIModel
import javax.inject.Inject

class UserAuthInteractor @Inject constructor(
    private val userAuthManager: UserAuthManager
) {

    fun isUserLoggedIn(): Boolean {
        return userAuthManager.isUserLoggedIn()
    }

    fun getCurrentUser(): BaseUIModel<UserAuthUIModel> {
        return try {
            val authUser = userAuthManager.getCurrentUser()
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
            userAuthManager.logout()
            Success(Unit)
        } catch (e: Exception) {
            Error("Logout failed: ${e.message}")
        }
    }

    suspend fun register(email: String, password: String): BaseUIModel<UserAuthUIModel> {
        return when (val result = userAuthManager.register(email, password)) {
            is ResultWrapper.Success -> {
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

            is ResultWrapper.Error -> {
                Error(result.message.orEmpty())
            }

            ResultWrapper.Loading -> Loading
        }
    }

    suspend fun login(email: String, password: String): BaseUIModel<UserAuthUIModel> {
        return when (val result = userAuthManager.login(email, password)) {
            is ResultWrapper.Success -> {
                // After successful login, get the current user
                when (val currentUserResult = getCurrentUser()) {
                    is Success -> currentUserResult
                    is Error -> currentUserResult
                    Empty -> Error("Failed to retrieve user data after login")
                    Loading -> Error("Unexpected loading state")
                }
            }

            is ResultWrapper.Error -> {
                Error(result.message.orEmpty())
            }

            ResultWrapper.Loading -> Loading
        }
    }

    suspend fun sendPasswordResetEmail(email: String): BaseUIModel<String> {
        return when (val result = userAuthManager.sendPasswordResetEmail(email)) {
            is ResultWrapper.Success -> {
                Success("Password reset email sent successfully")
            }

            is ResultWrapper.Error -> {
                Error(result.message.orEmpty())
            }

            ResultWrapper.Loading -> Loading
        }
    }

    suspend fun sendEmailVerification(): BaseUIModel<UserAuthUIModel> {
        return when (val result = userAuthManager.sendEmailVerification()) {
            is ResultWrapper.Success -> {
                when (val currentUserResult = getCurrentUser()) {
                    is Success -> currentUserResult
                    is Error -> currentUserResult
                    Empty -> Error("No user logged in")
                    Loading -> Error("Unexpected loading state")
                }
            }

            is ResultWrapper.Error -> {
                Error(result.message.orEmpty())
            }

            ResultWrapper.Loading -> Loading
        }
    }
}