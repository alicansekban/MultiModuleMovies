package com.alican.multimodulemovies.ui.security

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alican.multimodulemovies.theme.AppTheme
import kotlin.system.exitProcess

@Composable
fun SecurityScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Icon and Title
            SecurityHeader()

            Spacer(modifier = Modifier.height(32.dp))

            // Main Security Alert Card
            SecurityAlertCard()

            Spacer(modifier = Modifier.height(24.dp))

            // Security Issues List
            SecurityIssuesList()

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            SecurityActions()
        }
    }
}

@Composable
private fun SecurityHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.error
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = "Security Warning",
                modifier = Modifier.size(64.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Security Alert",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Access Denied",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun SecurityAlertCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Security Threat Detected",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your device or environment has been identified as potentially unsafe. " +
                        "To protect your data and ensure the security of the application, " +
                        "access has been restricted.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Justify
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
                    .padding(16.dp)
            ) {
                Text(
                    text = "⚠️ This security measure helps protect against unauthorized access, " +
                            "data theft, and malicious activities.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SecurityIssuesList() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Potential Security Issues",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            SecurityIssueItem(
                icon = Icons.Outlined.Android,
                title = "Rooted Device",
                description = "Device may have administrative access modifications"
            )

            SecurityIssueItem(
                icon = Icons.Outlined.PhoneAndroid,
                title = "Emulator Detection",
                description = "Running on an emulated environment"
            )

            SecurityIssueItem(
                icon = Icons.Outlined.BugReport,
                title = "Debugging Tools",
                description = "Active debugging or development tools detected"
            )

            SecurityIssueItem(
                icon = Icons.Outlined.Security,
                title = "App Cloning",
                description = "Application may be running in a cloned environment"
            )
        }
    }
}

@Composable
private fun SecurityIssueItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun SecurityActions() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What can you do?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "• Use the app on a non-rooted device\n" +
                            "• Disable debugging tools and developer options\n" +
                            "• Remove app cloning or virtual space applications\n" +
                            "• Run the app on a physical device instead of emulator",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = {
                    // Restart the app to re-check security
                    //killProcess(android.os.Process.myPid())
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Retry")
            }

            Button(
                onClick = {
                    // Exit the application
                    exitProcess(0)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Exit App")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "For assistance, please contact our support team.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

// MARK: - Previews

@Preview(name = "Security Screen Light")
@Composable
private fun SecurityScreenLightPreview() {
    AppTheme(isDarkMode = false) {
        SecurityScreen()
    }
}

@Preview(name = "Security Screen Dark")
@Composable
private fun SecurityScreenDarkPreview() {
    AppTheme(isDarkMode = true) {
        SecurityScreen()
    }
}

@Preview(name = "Security Header")
@Composable
private fun SecurityHeaderPreview() {
    AppTheme(isDarkMode = false) {
        SecurityHeader()
    }
}

@Preview(name = "Security Header Dark")
@Composable
private fun SecurityHeaderDarkPreview() {
    AppTheme(isDarkMode = true) {
        SecurityHeader()
    }
}

@Preview(name = "Security Alert Card")
@Composable
private fun SecurityAlertCardPreview() {
    AppTheme(isDarkMode = false) {
        SecurityAlertCard()
    }
}

@Preview(name = "Security Alert Card Dark")
@Composable
private fun SecurityAlertCardDarkPreview() {
    AppTheme(isDarkMode = true) {
        SecurityAlertCard()
    }
}

@Preview(name = "Security Issues List")
@Composable
private fun SecurityIssuesListPreview() {
    AppTheme(isDarkMode = false) {
        SecurityIssuesList()
    }
}

@Preview(name = "Security Issues List Dark")
@Composable
private fun SecurityIssuesListDarkPreview() {
    AppTheme(isDarkMode = true) {
        SecurityIssuesList()
    }
}

@Preview(name = "Security Issue Item")
@Composable
private fun SecurityIssueItemPreview() {
    AppTheme(isDarkMode = false) {
        SecurityIssueItem(
            icon = Icons.Outlined.Android,
            title = "Rooted Device",
            description = "Device may have administrative access modifications"
        )
    }
}

@Preview(name = "Security Actions")
@Composable
private fun SecurityActionsPreview() {
    AppTheme(isDarkMode = false) {
        SecurityActions()
    }
}

@Preview(name = "Security Actions Dark")
@Composable
private fun SecurityActionsDarkPreview() {
    AppTheme(isDarkMode = true) {
        SecurityActions()
    }
}