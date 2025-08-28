package com.alican.multimodulemovies.ui.help

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alican.multimodulemovies.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(
    onBackClicked: () -> Unit = {},
    onContactEmail: () -> Unit = {},
    onContactPhone: () -> Unit = {},
    onOpenWebsite: () -> Unit = {},
    onReportBug: () -> Unit = {},
    onSendFeedback: () -> Unit = {},
    onRateApp: () -> Unit = {},
) {
    Column(
        modifier = Modifier,
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Help & Support",
                    style = AppTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colorScheme.primaryText
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = AppTheme.colorScheme.primaryText
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = AppTheme.colorScheme.primaryBackground
            )
        )
        HelpSupportScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            onContactEmail = onContactEmail,
            onContactPhone = onContactPhone,
            onOpenWebsite = onOpenWebsite,
            onReportBug = onReportBug,
            onSendFeedback = onSendFeedback,
            onRateApp = onRateApp
        )
    }
}

@Composable
private fun HelpSupportScreenContent(
    modifier: Modifier = Modifier,
    onContactEmail: () -> Unit,
    onContactPhone: () -> Unit,
    onOpenWebsite: () -> Unit,
    onReportBug: () -> Unit,
    onSendFeedback: () -> Unit,
    onRateApp: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WelcomeCard()
        }

        item {
            FAQSection()
        }

        item {
            ContactSupportSection(
                onContactEmail = onContactEmail,
                onContactPhone = onContactPhone,
                onOpenWebsite = onOpenWebsite
            )
        }

        item {
            FeedbackSection(
                onReportBug = onReportBug,
                onSendFeedback = onSendFeedback,
                onRateApp = onRateApp
            )
        }

        item {
            TroubleshootingSection()
        }
    }
}

@Composable
private fun WelcomeCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.primaryButton.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colorScheme.primaryButton.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Support,
                    contentDescription = "Support",
                    modifier = Modifier.size(32.dp),
                    tint = AppTheme.colorScheme.primaryButton
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "How can we help you?",
                style = AppTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We're here to help you get the most out of MultiModule Movies. " +
                        "Browse our FAQ, contact support, or send us feedback.",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.secondaryText,
                textAlign = TextAlign.Center,
                lineHeight = AppTheme.typography.bodyMedium.lineHeight
            )
        }
    }
}

@Composable
private fun FAQSection(
    modifier: Modifier = Modifier
) {
    SectionCard(
        modifier = modifier,
        title = "Frequently Asked Questions",
        icon = Icons.Default.QuestionAnswer,
        iconTint = AppTheme.colorScheme.primaryButton
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            FAQItem(
                question = "How do I add movies to my favorites?",
                answer = "Tap the heart icon on any movie card to add or remove it from your favorites collection."
            )
            FAQItem(
                question = "Why can't I see some movie details?",
                answer = "Some information might not be available from our data source. We continuously work to provide the most complete data possible."
            )
            FAQItem(
                question = "How do I switch between light and dark theme?",
                answer = "Go to Profile → Settings → Theme to toggle between light and dark modes."
            )
            FAQItem(
                question = "Can I use the app offline?",
                answer = "Your favorite movies are stored locally and can be viewed offline. However, browsing new movies requires an internet connection."
            )
            FAQItem(
                question = "How do I search for specific movies?",
                answer = "Use the Search tab to find movies by title. The search results update as you type."
            )
        }
    }
}

@Composable
private fun ContactSupportSection(
    modifier: Modifier = Modifier,
    onContactEmail: () -> Unit,
    onContactPhone: () -> Unit,
    onOpenWebsite: () -> Unit
) {
    SectionCard(
        modifier = modifier,
        title = "Contact Support",
        icon = Icons.Default.Chat,
        iconTint = AppTheme.colorScheme.successColor
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ContactItem(
                icon = Icons.Default.Email,
                title = "Email Support",
                subtitle = "support@multimodulemovies.com",
                onClick = onContactEmail
            )
            ContactItem(
                icon = Icons.Default.Phone,
                title = "Phone Support",
                subtitle = "+1 (555) 123-4567",
                onClick = onContactPhone
            )
            ContactItem(
                icon = Icons.Default.Web,
                title = "Visit Website",
                subtitle = "www.multimodulemovies.com",
                onClick = onOpenWebsite
            )
        }
    }
}

@Composable
private fun FeedbackSection(
    modifier: Modifier = Modifier,
    onReportBug: () -> Unit,
    onSendFeedback: () -> Unit,
    onRateApp: () -> Unit
) {
    SectionCard(
        modifier = modifier,
        title = "Feedback & Reviews",
        icon = Icons.Default.Feedback,
        iconTint = AppTheme.colorScheme.accent
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ContactItem(
                icon = Icons.Default.BugReport,
                title = "Report a Bug",
                subtitle = "Help us improve by reporting issues",
                onClick = onReportBug
            )
            ContactItem(
                icon = Icons.Default.Feedback,
                title = "Send Feedback",
                subtitle = "Share your thoughts and suggestions",
                onClick = onSendFeedback
            )
            ContactItem(
                icon = Icons.Default.Star,
                title = "Rate Our App",
                subtitle = "Leave a review on Google Play Store",
                onClick = onRateApp
            )
        }
    }
}

@Composable
private fun TroubleshootingSection(
    modifier: Modifier = Modifier
) {
    SectionCard(
        modifier = modifier,
        title = "Troubleshooting",
        icon = Icons.Default.Help,
        iconTint = AppTheme.colorScheme.warningColor
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TroubleshootingItem(
                title = "App crashes or freezes",
                steps = listOf(
                    "Close and restart the app",
                    "Clear app cache from device settings",
                    "Update to the latest version",
                    "Restart your device if needed"
                )
            )
            TroubleshootingItem(
                title = "Movies not loading",
                steps = listOf(
                    "Check your internet connection",
                    "Try switching between WiFi and mobile data",
                    "Wait a moment and try refreshing",
                    "Contact support if issue persists"
                )
            )
            TroubleshootingItem(
                title = "Search not working",
                steps = listOf(
                    "Check your spelling",
                    "Try different keywords",
                    "Ensure you have an internet connection",
                    "Clear the search and try again"
                )
            )
        }
    }
}

@Composable
private fun SectionCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardSecondaryBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(18.dp),
                        tint = iconTint
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = AppTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colorScheme.primaryText
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable
private fun FAQItem(
    question: String,
    answer: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Q: $question",
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colorScheme.primaryText
        )
        Text(
            text = "A: $answer",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.secondaryText,
            lineHeight = AppTheme.typography.bodyMedium.lineHeight
        )
    }
}

@Composable
private fun ContactItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(20.dp),
                tint = AppTheme.colorScheme.primaryButton
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = AppTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colorScheme.primaryText
                )
                Text(
                    text = subtitle,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colorScheme.secondaryText
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                modifier = Modifier.size(16.dp),
                tint = AppTheme.colorScheme.secondaryText
            )
        }
    }
}

@Composable
private fun TroubleshootingItem(
    title: String,
    steps: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colorScheme.primaryText
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 8.dp)
        ) {
            steps.forEachIndexed { index, step ->
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "${index + 1}.",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colorScheme.primaryButton,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = step,
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colorScheme.secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// Preview Composables
@Preview(name = "Help Support Screen Light")
@Composable
private fun HelpSupportScreenLightPreview() {
    AppTheme(isDarkMode = false) {
        HelpSupportScreenContent(
            onContactEmail = {},
            onContactPhone = {},
            onOpenWebsite = {},
            onReportBug = {},
            onSendFeedback = {},
            onRateApp = {}
        )
    }
}

@Preview(name = "Help Support Screen Dark")
@Composable
private fun HelpSupportScreenDarkPreview() {
    AppTheme(isDarkMode = true) {
        HelpSupportScreenContent(
            onContactEmail = {},
            onContactPhone = {},
            onOpenWebsite = {},
            onReportBug = {},
            onSendFeedback = {},
            onRateApp = {}
        )
    }
}

@Preview(name = "Welcome Card")
@Composable
private fun WelcomeCardPreview() {
    AppTheme(isDarkMode = false) {
        WelcomeCard()
    }
}

@Preview(name = "FAQ Item")
@Composable
private fun FAQItemPreview() {
    AppTheme(isDarkMode = false) {
        FAQItem(
            question = "How do I add movies to my favorites?",
            answer = "Tap the heart icon on any movie card to add or remove it from your favorites collection."
        )
    }
}

@Preview(name = "Contact Item")
@Composable
private fun ContactItemPreview() {
    AppTheme(isDarkMode = false) {
        ContactItem(
            icon = Icons.Default.Email,
            title = "Email Support",
            subtitle = "support@multimodulemovies.com",
            onClick = {}
        )
    }
}