package com.alican.multimodulemovies.ui.register

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
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RegisterScreenContent(
        uiState = uiState,
        onEvent = viewModel::onScreenEvent
    )

    // Clear error when screen recomposes
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            kotlinx.coroutines.delay(3000) // Clear error after 3 seconds
            viewModel.onScreenEvent(RegisterUIEvents.ClearError)
        }
    }
}

@Composable
fun RegisterScreenContent(
    uiState: RegisterUIState,
    onEvent: (RegisterUIEvents) -> Unit = {}
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
                    text = "Create Account",
                    style = AppTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colorScheme.primaryText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sign up to get started",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.secondaryText,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Register Form
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
                    onValueChange = { onEvent(RegisterUIEvents.UpdateEmail(it)) },
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
                    onValueChange = { onEvent(RegisterUIEvents.UpdatePassword(it)) },
                    label = "Password",
                    leadingIcon = Icons.Default.Lock,
                    trailingIcon = {
                        PasswordTrailingIcon(
                            isVisible = uiState.isPasswordVisible,
                            onToggle = { onEvent(RegisterUIEvents.TogglePasswordVisibility) }
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    maxLength = 50
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Field
                DynamicTextField(
                    value = uiState.confirmPassword,
                    onValueChange = { onEvent(RegisterUIEvents.UpdateConfirmPassword(it)) },
                    label = "Confirm Password",
                    leadingIcon = Icons.Default.Lock,
                    trailingIcon = {
                        PasswordTrailingIcon(
                            isVisible = uiState.isConfirmPasswordVisible,
                            onToggle = { onEvent(RegisterUIEvents.ToggleConfirmPasswordVisibility) }
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    maxLength = 50
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

                // Register Button
                Button(
                    onClick = { onEvent(RegisterUIEvents.Register) },
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
                            text = "Create Account",
                            style = AppTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colorScheme.primaryBackground
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Option
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
                    text = "Already have an account? ",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.secondaryText
                )
                TextButton(
                    onClick = { onEvent(RegisterUIEvents.NavigateToLogin) }
                ) {
                    Text(
                        text = "Sign In",
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

@Preview(name = "Register Screen Light")
@Composable
private fun RegisterScreenPreview() {
    AppTheme {
        RegisterScreenContent(
            uiState = RegisterUIState(
                email = "user@example.com",
                password = "password123",
                confirmPassword = "password123",
                isPasswordVisible = false,
                isConfirmPasswordVisible = false,
                isLoading = false,
                errorMessage = null
            )
        )
    }
}

@Preview(name = "Register Screen Dark")
@Composable
private fun RegisterScreenDarkPreview() {
    AppTheme(isDarkMode = true) {
        RegisterScreenContent(
            uiState = RegisterUIState(
                email = "user@example.com",
                password = "password123",
                confirmPassword = "password12",
                isPasswordVisible = false,
                isConfirmPasswordVisible = false,
                isLoading = false,
                errorMessage = "Passwords don't match"
            )
        )
    }
}