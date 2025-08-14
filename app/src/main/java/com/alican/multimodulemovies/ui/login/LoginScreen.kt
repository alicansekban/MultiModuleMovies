package com.alican.multimodulemovies.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.multimodulemovies.components.textfield.DynamicTextField
import com.alican.multimodulemovies.components.textfield.PasswordTrailingIcon
import com.alican.multimodulemovies.theme.AppTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreenContent(
        uiState = uiState,
        onEvent = viewModel::onScreenEvent
    )

    // Clear error when screen recomposes
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            kotlinx.coroutines.delay(3000) // Clear error after 3 seconds
            viewModel.onScreenEvent(LoginUIEvents.ClearError)
        }
    }
}

@Composable
fun LoginScreenContent(
    uiState: LoginUIState,
    onEvent: (LoginUIEvents) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Title Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colorScheme.cardBackground
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome Back!",
                    style = AppTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colorScheme.primaryText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sign in to continue",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.secondaryText,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login Form
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colorScheme.cardBackground
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Email Field
                DynamicTextField(
                    value = uiState.email,
                    onValueChange = { onEvent(LoginUIEvents.UpdateEmail(it)) },
                    label = "Email",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    maxLength = 100
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                DynamicTextField(
                    value = uiState.password,
                    onValueChange = { onEvent(LoginUIEvents.UpdatePassword(it)) },
                    label = "Password",
                    leadingIcon = Icons.Default.Lock,
                    trailingIcon = {
                        PasswordTrailingIcon(
                            isVisible = uiState.isPasswordVisible,
                            onToggle = { onEvent(LoginUIEvents.TogglePasswordVisibility) }
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    maxLength = 50,
                )

                // Error Message
                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.errorMessage,
                        color = AppTheme.colorScheme.errorColor,
                        style = AppTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Button(
                    onClick = { onEvent(LoginUIEvents.Login) },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colorScheme.primaryButton
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = AppTheme.colorScheme.primaryBackground
                        )
                    } else {
                        Text(
                            text = "Sign In",
                            style = AppTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colorScheme.primaryBackground
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Register Option
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colorScheme.cardSecondaryBackground
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.secondaryText
                )
                TextButton(
                    onClick = { onEvent(LoginUIEvents.NavigateToRegister) }
                ) {
                    Text(
                        text = "Sign Up",
                        style = AppTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colorScheme.primaryButton
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(name = "Login Screen Light")
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreenContent(
            uiState = LoginUIState(
                email = "user@example.com",
                password = "password123",
                isPasswordVisible = false,
                isLoading = false,
                errorMessage = null
            )
        )
    }
}

@Preview(name = "Login Screen Dark")
@Composable
private fun LoginScreenDarkPreview() {
    AppTheme(isDarkMode = true) {
        LoginScreenContent(
            uiState = LoginUIState(
                email = "user@example.com",
                password = "password123",
                isPasswordVisible = false,
                isLoading = false,
                errorMessage = "Invalid credentials"
            )
        )
    }
}