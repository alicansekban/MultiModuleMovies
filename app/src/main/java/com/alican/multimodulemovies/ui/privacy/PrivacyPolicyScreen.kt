package com.alican.multimodulemovies.ui.privacy

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storage
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
fun PrivacyPolicyScreen(
    onBackClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier,
    ) {

        TopAppBar(
            title = {
                Text(
                    text = "Privacy Policy",
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
        PrivacyPolicyScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Composable
private fun PrivacyPolicyScreenContent(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            IntroductionCard()
        }

        item {
            LastUpdatedCard()
        }

        item {
            DataCollectionSection()
        }

        item {
            DataUsageSection()
        }

        item {
            DataStorageSection()
        }

        item {
            CookiesSection()
        }

        item {
            SecuritySection()
        }

        item {
            UserRightsSection()
        }

        item {
            ThirdPartySection()
        }

        item {
            ContactSection()
        }
    }
}

@Composable
private fun IntroductionCard(
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
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Privacy Shield",
                    modifier = Modifier.size(32.dp),
                    tint = AppTheme.colorScheme.primaryButton
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Privacy Matters",
                style = AppTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We are committed to protecting your privacy and being transparent about how we collect, use, and share your information. This policy explains our practices regarding your personal data when you use MultiModule Movies.",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.secondaryText,
                textAlign = TextAlign.Center,
                lineHeight = AppTheme.typography.bodyMedium.lineHeight
            )
        }
    }
}

@Composable
private fun LastUpdatedCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Last Updated",
                modifier = Modifier.size(20.dp),
                tint = AppTheme.colorScheme.secondaryText
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Last Updated",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colorScheme.secondaryText,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "September 2025",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.primaryText,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun DataCollectionSection(
    modifier: Modifier = Modifier
) {
    PolicySection(
        modifier = modifier,
        title = "Information We Collect",
        icon = Icons.Default.DataUsage,
        iconTint = AppTheme.colorScheme.primaryButton
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PolicySubsection(
                title = "Personal Information",
                content = "When you create an account, we collect your email address, name, and any profile information you choose to provide."
            )
            PolicySubsection(
                title = "Usage Data",
                content = "We collect information about how you use our app, including your movie preferences, search queries, and favorite movies to improve your experience."
            )
            PolicySubsection(
                title = "Device Information",
                content = "We may collect device-specific information such as your device model, operating system version, and unique device identifiers for analytics and app optimization."
            )
            PolicySubsection(
                title = "Movie Preferences",
                content = "Your favorite movies, ratings, and viewing preferences are stored to personalize your experience and provide better recommendations."
            )
        }
    }
}

@Composable
private fun DataUsageSection(
    modifier: Modifier = Modifier
) {
    PolicySection(
        modifier = modifier,
        title = "How We Use Your Information",
        icon = Icons.Default.Info,
        iconTint = AppTheme.colorScheme.successColor
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BulletPoint("Provide and maintain our movie discovery service")
            BulletPoint("Personalize your movie recommendations and experience")
            BulletPoint("Process and respond to your support requests")
            BulletPoint("Send you important app updates and notifications")
            BulletPoint("Improve our app's functionality and user experience")
            BulletPoint("Analyze usage patterns to enhance our services")
            BulletPoint("Ensure the security and integrity of our platform")
        }
    }
}

@Composable
private fun DataStorageSection(
    modifier: Modifier = Modifier
) {
    PolicySection(
        modifier = modifier,
        title = "Data Storage & Retention",
        icon = Icons.Default.Storage,
        iconTint = AppTheme.colorScheme.accent
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PolicySubsection(
                title = "Local Storage",
                content = "Your favorite movies and app preferences are stored locally on your device for offline access and quick loading."
            )
            PolicySubsection(
                title = "Cloud Storage",
                content = "Account information and synchronized data are securely stored in the cloud to provide seamless experience across devices."
            )
            PolicySubsection(
                title = "Retention Period",
                content = "We retain your personal information only as long as necessary to provide our services or as required by law. You can delete your account at any time."
            )
        }
    }
}

@Composable
private fun CookiesSection(
    modifier: Modifier = Modifier
) {
    PolicySection(
        modifier = modifier,
        title = "Cookies & Tracking",
        icon = Icons.Default.Cookie,
        iconTint = AppTheme.colorScheme.warningColor
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PolicySubsection(
                title = "Analytics",
                content = "We use analytics tools to understand how users interact with our app, helping us improve functionality and user experience."
            )
            PolicySubsection(
                title = "Preferences",
                content = "We store your app settings and preferences locally to maintain your personalized experience across app sessions."
            )
            PolicySubsection(
                title = "Third-Party Services",
                content = "Our app integrates with The Movie Database (TMDB) and other services that may use their own tracking technologies."
            )
        }
    }
}

@Composable
private fun SecuritySection(
    modifier: Modifier = Modifier
) {
    PolicySection(
        modifier = modifier,
        title = "Security Measures",
        icon = Icons.Default.Security,
        iconTint = AppTheme.colorScheme.errorColor
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BulletPoint("Industry-standard encryption for data transmission")
            BulletPoint("Secure authentication and authorization systems")
            BulletPoint("Regular security audits and vulnerability assessments")
            BulletPoint("Limited access to personal information by our team")
            BulletPoint("Secure cloud infrastructure with regular backups")
            BulletPoint("Monitoring for unauthorized access attempts")
        }
    }
}

@Composable
private fun UserRightsSection(
    modifier: Modifier = Modifier
) {
    PolicySection(
        modifier = modifier,
        title = "Your Rights & Choices",
        icon = Icons.Default.Gavel,
        iconTint = AppTheme.colorScheme.primaryButton
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BulletPoint("Access your personal information and data we have collected")
            BulletPoint("Update or correct your account information at any time")
            BulletPoint("Delete your account and associated data")
            BulletPoint("Opt-out of non-essential communications")
            BulletPoint("Control your privacy settings within the app")
            BulletPoint("Request a copy of your data in a portable format")
            BulletPoint("Contact us with privacy concerns or questions")
        }
    }
}

@Composable
private fun ThirdPartySection(
    modifier: Modifier = Modifier
) {
    PolicySection(
        modifier = modifier,
        title = "Third-Party Services",
        icon = Icons.Default.Lock,
        iconTint = AppTheme.colorScheme.successColor
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PolicySubsection(
                title = "The Movie Database (TMDB)",
                content = "We use TMDB API to provide movie information. Please review TMDB's privacy policy for their data practices."
            )
            PolicySubsection(
                title = "Analytics Services",
                content = "We may use third-party analytics services to help us understand app usage. These services have their own privacy policies."
            )
            PolicySubsection(
                title = "Authentication Services",
                content = "If you sign in with Google or other providers, their privacy policies apply to the information they share with us."
            )
        }
    }
}

@Composable
private fun ContactSection(
    modifier: Modifier = Modifier
) {
    PolicySection(
        modifier = modifier,
        title = "Contact Us",
        icon = Icons.Default.Email,
        iconTint = AppTheme.colorScheme.accent
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "If you have any questions about this Privacy Policy or our privacy practices, please contact us:",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.primaryText,
                lineHeight = AppTheme.typography.bodyMedium.lineHeight
            )

            PolicySubsection(
                title = "Email",
                content = "privacy@multimodulemovies.com"
            )
            PolicySubsection(
                title = "Address",
                content = "MultiModule Movies\n123 Privacy Street\nData Protection City, DP 12345"
            )

            Text(
                text = "We will respond to your privacy-related inquiries within 30 days.",
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colorScheme.secondaryText,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

@Composable
private fun PolicySection(
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
private fun PolicySubsection(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colorScheme.primaryText
        )
        Text(
            text = content,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.secondaryText,
            lineHeight = AppTheme.typography.bodyMedium.lineHeight
        )
    }
}

@Composable
private fun BulletPoint(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "•",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.primaryButton,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.primaryText,
            modifier = Modifier.weight(1f),
            lineHeight = AppTheme.typography.bodyMedium.lineHeight
        )
    }
}

// Preview Composables
@Preview(name = "Privacy Policy Screen Light")
@Composable
private fun PrivacyPolicyScreenLightPreview() {
    AppTheme(isDarkMode = false) {
        PrivacyPolicyScreenContent()
    }
}

@Preview(name = "Privacy Policy Screen Dark")
@Composable
private fun PrivacyPolicyScreenDarkPreview() {
    AppTheme(isDarkMode = true) {
        PrivacyPolicyScreenContent()
    }
}

@Preview(name = "Introduction Card")
@Composable
private fun IntroductionCardPreview() {
    AppTheme(isDarkMode = false) {
        IntroductionCard()
    }
}

@Preview(name = "Policy Subsection")
@Composable
private fun PolicySubsectionPreview() {
    AppTheme(isDarkMode = false) {
        PolicySubsection(
            title = "Personal Information",
            content = "When you create an account, we collect your email address, name, and any profile information you choose to provide."
        )
    }
}

@Preview(name = "Bullet Point")
@Composable
private fun BulletPointPreview() {
    AppTheme(isDarkMode = false) {
        BulletPoint("Provide and maintain our movie discovery service")
    }
}