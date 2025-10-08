package com.alican.multimodulemovies.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.ui_models.user.UserAuthUIModel
import com.alican.multimodulemovies.theme.AppTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle navigation to login
    LaunchedEffect(Unit) {
        // This can be used for one-time events if needed
    }

    ProfileScreenContent(
        uiState = uiState,
        onEvent = viewModel::onScreenEvent
    )

    // Clear error when screen recomposes
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            kotlinx.coroutines.delay(3000) // Clear error after 5 seconds
            viewModel.onScreenEvent(ProfileUIEvents.ClearError)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    uiState: ProfileUIState,
    onEvent: (ProfileUIEvents) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Show error if exists
            uiState.error?.let { error ->
                ErrorCard(
                    error = error,
                    onDismiss = { onEvent(ProfileUIEvents.ClearError) }
                )
            }

            // User Info Section
            UserInfoSection(
                isLoggedIn = uiState.isUserLoggedIn,
                userName = uiState.userName,
                userSurname = uiState.userSurname,
                userImageUrl = uiState.userImageUrl,
                userEmail = uiState.currentUser?.email,
                onLoginClick = { onEvent(ProfileUIEvents.NavigateToLogin) }
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SectionHeader(title = "Settings")
                }

                item {
                    SettingsItem(
                        icon = if (uiState.isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        title = "Theme",
                        subtitle = if (uiState.isDarkTheme) "Dark Mode" else "Light Mode",
                        onClick = { onEvent(ProfileUIEvents.ToggleTheme) },
                        iconTint = AppTheme.colorScheme.accent
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Manage your notification preferences",
                        onClick = { onEvent(ProfileUIEvents.HandleNotifications) },
                        iconTint = AppTheme.colorScheme.warningColor
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Help,
                        title = "Help & Support",
                        subtitle = "Get help and contact support",
                        onClick = { onEvent(ProfileUIEvents.HandleHelp) },
                        iconTint = AppTheme.colorScheme.primaryButton
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "About",
                        subtitle = "App version and information",
                        onClick = { onEvent(ProfileUIEvents.HandleAbout) },
                        iconTint = AppTheme.colorScheme.secondaryText
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Security,
                        title = "Privacy Policy",
                        subtitle = "Read our privacy policy",
                        onClick = { onEvent(ProfileUIEvents.HandlePrivacyPolicy) },
                        iconTint = AppTheme.colorScheme.successColor
                    )
                }

                if (uiState.isUserLoggedIn) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))

                        LogoutButton(
                            isLoading = uiState.isLoading,
                            onLogout = { onEvent(ProfileUIEvents.Logout) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorCard(
    error: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = error,
                style = AppTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun UserInfoSection(
    isLoggedIn: Boolean,
    userName: String,
    userSurname: String,
    userImageUrl: String?,
    userEmail: String?,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserAvatar(userImageUrl, isLoggedIn)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$userName $userSurname",
                style = AppTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText
            )

            if (isLoggedIn && userEmail != null) {
                Text(
                    text = userEmail,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.secondaryText
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            StatusBadge(isLoggedIn)

            // Login/Register button for guest users
            if (!isLoggedIn) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colorScheme.primaryButton
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = "Login",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Login / Register",
                        style = AppTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun UserAvatar(
    userImageUrl: String?,
    isLoggedIn: Boolean
) {
    Card(
        modifier = Modifier.size(80.dp),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLoggedIn)
                AppTheme.colorScheme.primaryButton.copy(alpha = 0.1f)
            else
                AppTheme.colorScheme.cardSecondaryBackground
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (userImageUrl != null) {
                // TODO: Add AsyncImage for user profile image
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(40.dp),
                    tint = AppTheme.colorScheme.primaryButton
                )
            } else {
                Icon(
                    imageVector = if (isLoggedIn) Icons.Default.Person else Icons.Default.PersonOff,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(40.dp),
                    tint = if (isLoggedIn) AppTheme.colorScheme.primaryButton else AppTheme.colorScheme.secondaryText
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(isLoggedIn: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isLoggedIn)
                AppTheme.colorScheme.statusOnline.copy(alpha = 0.1f)
            else
                AppTheme.colorScheme.statusOffline.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (isLoggedIn) AppTheme.colorScheme.statusOnline else AppTheme.colorScheme.statusOffline,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isLoggedIn) "Online" else "Guest",
                style = AppTheme.typography.bodySmall,
                color = if (isLoggedIn) AppTheme.colorScheme.statusOnline else AppTheme.colorScheme.statusOffline,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = AppTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = AppTheme.colorScheme.primaryText
    )
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    iconTint: androidx.compose.ui.graphics.Color = AppTheme.colorScheme.primaryButton,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardSecondaryBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with background circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(20.dp),
                    tint = iconTint
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = AppTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colorScheme.primaryText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colorScheme.secondaryText
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                modifier = Modifier.size(20.dp),
                tint = AppTheme.colorScheme.secondaryText
            )
        }
    }
}

@Composable
private fun LogoutButton(
    isLoading: Boolean,
    onLogout: () -> Unit
) {
    Button(
        onClick = onLogout,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onError
            )
        } else {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isLoading) "Logging out..." else "Logout",
            style = AppTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

// Preview Composables
@Preview(name = "Profile Screen Light - Logged In")
@Composable
private fun ProfileScreenLoggedInPreview() {
    AppTheme(isDarkMode = false) {
        ProfileScreenContent(
            uiState = ProfileUIState(
                isUserLoggedIn = true,
                currentUser = UserAuthUIModel(
                    uid = "1",
                    email = "john.doe@example.com",
                    displayName = "John Doe",
                    isEmailVerified = true,
                    isLoggedIn = true
                ),
                userName = "John",
                userSurname = "Doe",
                userImageUrl = null,
                isDarkTheme = false,
                isLoading = false,
                error = null
            )
        )
    }
}

@Preview(name = "Profile Screen Dark - Logged In")
@Composable
private fun ProfileScreenLoggedInDarkPreview() {
    AppTheme(isDarkMode = true) {
        ProfileScreenContent(
            uiState = ProfileUIState(
                isUserLoggedIn = true,
                currentUser = UserAuthUIModel(
                    uid = "1",
                    email = "john.doe@example.com",
                    displayName = "John Doe",
                    isEmailVerified = true,
                    isLoggedIn = true

                ),
                userName = "John",
                userSurname = "Doe",
                userImageUrl = null,
                isDarkTheme = true,
                isLoading = false,
                error = null
            )
        )
    }
}

@Preview(name = "Profile Screen Light - Guest User")
@Composable
private fun ProfileScreenGuestPreview() {
    AppTheme(isDarkMode = false) {
        ProfileScreenContent(
            uiState = ProfileUIState(
                isUserLoggedIn = false,
                currentUser = null,
                userName = "Guest",
                userSurname = "User",
                userImageUrl = null,
                isDarkTheme = false,
                isLoading = false,
                error = null
            )
        )
    }
}

@Preview(name = "Profile Screen Dark - Guest User")
@Composable
private fun ProfileScreenGuestDarkPreview() {
    AppTheme(isDarkMode = true) {
        ProfileScreenContent(
            uiState = ProfileUIState(
                isUserLoggedIn = false,
                currentUser = null,
                userName = "Guest",
                userSurname = "User",
                userImageUrl = null,
                isDarkTheme = true,
                isLoading = false,
                error = null
            )
        )
    }
}

@Preview(name = "Profile Screen Light - Loading")
@Composable
private fun ProfileScreenLoadingPreview() {
    AppTheme(isDarkMode = false) {
        ProfileScreenContent(
            uiState = ProfileUIState(
                isUserLoggedIn = true,
                currentUser = UserAuthUIModel(
                    uid = "1",
                    email = "john.doe@example.com",
                    displayName = "John Doe",
                    isEmailVerified = true,
                    isLoggedIn = true

                ),
                userName = "John",
                userSurname = "Doe",
                userImageUrl = null,
                isDarkTheme = false,
                isLoading = true,
                error = null
            )
        )
    }
}

@Preview(name = "Profile Screen Light - With Error")
@Composable
private fun ProfileScreenErrorPreview() {
    AppTheme(isDarkMode = false) {
        ProfileScreenContent(
            uiState = ProfileUIState(
                isUserLoggedIn = false,
                currentUser = null,
                userName = "Guest",
                userSurname = "User",
                userImageUrl = null,
                isDarkTheme = false,
                isLoading = false,
                error = "Failed to load user data. Please try again."
            )
        )
    }
}