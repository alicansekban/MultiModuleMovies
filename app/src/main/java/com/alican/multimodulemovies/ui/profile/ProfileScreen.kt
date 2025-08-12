package com.alican.multimodulemovies.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.alican.multimodulemovies.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            // Handle error display
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
    ) {
        // Loading indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = AppTheme.colorScheme.primaryButton
                )
            }
        }

        // Error display
        uiState.error?.let { error ->
            ErrorCard(
                error = error,
                onDismiss = { viewModel.clearError() }
            )
        }

        // Top Section - User Info
        UserInfoSection(
            isLoggedIn = uiState.isUserLoggedIn,
            userName = uiState.userName,
            userSurname = uiState.userSurname,
            userImageUrl = uiState.userImageUrl,
            userEmail = uiState.currentUser?.email,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Settings Section
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
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
                    onClick = { viewModel.toggleTheme() },
                    iconTint = AppTheme.colorScheme.accent
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Manage your notification preferences",
                    onClick = { /* Handle notifications */ },
                    iconTint = AppTheme.colorScheme.warningColor
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Default.Help,
                    title = "Help & Support",
                    subtitle = "Get help and contact support",
                    onClick = { /* Handle help */ },
                    iconTint = AppTheme.colorScheme.primaryButton
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "About",
                    subtitle = "App version and information",
                    onClick = { /* Handle about */ },
                    iconTint = AppTheme.colorScheme.secondaryText
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Default.Security,
                    title = "Privacy Policy",
                    subtitle = "Read our privacy policy",
                    onClick = { /* Handle privacy policy */ },
                    iconTint = AppTheme.colorScheme.successColor
                )
            }

            if (uiState.isUserLoggedIn) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    LogoutButton(
                        isLoading = uiState.isLoading,
                        onLogout = { viewModel.logout() }
                    )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.errorColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = AppTheme.colorScheme.errorColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = error,
                color = AppTheme.colorScheme.primaryText,
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear Error",
                    tint = AppTheme.colorScheme.errorColor,
                    modifier = Modifier.size(18.dp)
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Avatar
            UserAvatar(
                userImageUrl = userImageUrl,
                isLoggedIn = isLoggedIn
            )

            Spacer(modifier = Modifier.height(20.dp))

            // User Name
            Text(
                text = "$userName $userSurname",
                style = AppTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText,
                textAlign = TextAlign.Center
            )

            // User Email or Status
            Spacer(modifier = Modifier.height(6.dp))
            if (isLoggedIn && userEmail != null) {
                Text(
                    text = userEmail,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.secondaryText,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Not signed in",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.secondaryText,
                    textAlign = TextAlign.Center
                )
            }

            // User Status Badge
            Spacer(modifier = Modifier.height(16.dp))
            StatusBadge(isLoggedIn = isLoggedIn)
        }
    }
}

@Composable
private fun UserAvatar(
    userImageUrl: String?,
    isLoggedIn: Boolean
) {
    if (userImageUrl != null) {
        AsyncImage(
            model = userImageUrl,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(
                    width = 3.dp,
                    color = AppTheme.colorScheme.accent,
                    shape = CircleShape
                ),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    if (isLoggedIn) AppTheme.colorScheme.primaryButton
                    else AppTheme.colorScheme.secondaryBackground
                )
                .border(
                    width = 3.dp,
                    color = AppTheme.colorScheme.accent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isLoggedIn) Icons.Default.Person else Icons.Default.PersonOutline,
                contentDescription = "User Avatar",
                modifier = Modifier.size(45.dp),
                tint = if (isLoggedIn) AppTheme.colorScheme.primaryBackground
                else AppTheme.colorScheme.primaryText
            )
        }
    }
}

@Composable
private fun StatusBadge(isLoggedIn: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isLoggedIn)
                AppTheme.colorScheme.successColor.copy(alpha = 0.1f)
            else
                AppTheme.colorScheme.statusOffline.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (isLoggedIn) AppTheme.colorScheme.statusOnline
                        else AppTheme.colorScheme.statusOffline
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isLoggedIn) "Logged In" else "Guest User",
                style = AppTheme.typography.bodySmall,
                color = if (isLoggedIn) AppTheme.colorScheme.successColor
                else AppTheme.colorScheme.statusOffline,
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
        color = AppTheme.colorScheme.primaryText,
        modifier = Modifier.padding(vertical = 8.dp)
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
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colorScheme.errorColor,
            contentColor = AppTheme.colorScheme.primaryBackground
        ),
        shape = RoundedCornerShape(16.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = AppTheme.colorScheme.primaryBackground,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Logout",
                fontWeight = FontWeight.SemiBold,
                style = AppTheme.typography.titleSmall
            )
        }
    }
}