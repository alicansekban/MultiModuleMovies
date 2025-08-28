package com.alican.multimodulemovies.ui.about_us

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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alican.multimodulemovies.BuildConfig
import com.alican.multimodulemovies.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClicked: () -> Unit = {},
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "About",
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
        AboutScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            appName = context.getString(context.applicationInfo.labelRes),
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE.toString(),
            buildType = BuildConfig.BUILD_TYPE
        )
    }
}

@Composable
private fun AboutScreenContent(
    modifier: Modifier = Modifier,
    appName: String,
    versionName: String,
    versionCode: String,
    buildType: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AppInfoHeader(appName = appName)
            }

            item {
                AppDescriptionCard()
            }

            item {
                AppFeaturesCard()
            }

            item {
                DeveloperInfoCard()
            }

            item {
                TechnicalInfoCard()
            }
        }

        // Build information at bottom
        BuildInfoFooter(
            versionName = versionName,
            versionCode = versionCode,
            buildType = buildType
        )
    }
}

@Composable
private fun AppInfoHeader(
    appName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App icon placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppTheme.colorScheme.primaryButton.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "App Icon",
                    modifier = Modifier.size(40.dp),
                    tint = AppTheme.colorScheme.primaryButton
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = appName,
                style = AppTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your ultimate movie companion",
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colorScheme.secondaryText,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AppDescriptionCard(
    modifier: Modifier = Modifier
) {
    InfoCard(
        modifier = modifier,
        title = "About This App",
        icon = Icons.Default.Info,
        iconTint = AppTheme.colorScheme.primaryButton
    ) {
        Text(
            text = "MultiModule Movies is a modern Android application built with the latest technologies. " +
                    "Discover trending movies, manage your favorites, search for specific titles, and explore " +
                    "detailed information about your favorite films. Built with Jetpack Compose, Hilt, and " +
                    "following Clean Architecture principles.",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.primaryText,
            lineHeight = AppTheme.typography.bodyMedium.lineHeight
        )
    }
}

@Composable
private fun AppFeaturesCard(
    modifier: Modifier = Modifier
) {
    InfoCard(
        modifier = modifier,
        title = "Key Features",
        icon = Icons.Default.Star,
        iconTint = AppTheme.colorScheme.accent
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FeatureItem("Browse trending and popular movies")
            FeatureItem("Search movies with real-time results")
            FeatureItem("Manage your favorite movies collection")
            FeatureItem("View detailed movie information")
            FeatureItem("Dark and light theme support")
            FeatureItem("Offline favorites storage")
        }
    }
}

@Composable
private fun DeveloperInfoCard(
    modifier: Modifier = Modifier
) {
    InfoCard(
        modifier = modifier,
        title = "Developer",
        icon = Icons.Default.Person,
        iconTint = AppTheme.colorScheme.successColor
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoRow(
                label = "Name:",
                value = "Alican Sekban"
            )
            InfoRow(
                label = "Email:",
                value = "alicansekban@hotmail.com"
            )
            InfoRow(
                label = "GitHub:",
                value = "@alicansekban"
            )
        }
    }
}

@Composable
private fun TechnicalInfoCard(
    modifier: Modifier = Modifier
) {
    InfoCard(
        modifier = modifier,
        title = "Technical Details",
        icon = Icons.Default.Code,
        iconTint = AppTheme.colorScheme.warningColor
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoRow(
                label = "Platform:",
                value = "Android"
            )
            InfoRow(
                label = "Language:",
                value = "Kotlin"
            )
            InfoRow(
                label = "UI Framework:",
                value = "Jetpack Compose"
            )
            InfoRow(
                label = "Architecture:",
                value = "Clean Architecture + MVVM"
            )
            InfoRow(
                label = "Dependency Injection:",
                value = "Hilt"
            )
            InfoRow(
                label = "Networking:",
                value = "Ktor"
            )
            InfoRow(
                label = "API:",
                value = "The Movie Database (TMDB)"
            )
        }
    }
}

@Composable
private fun InfoCard(
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

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

@Composable
private fun FeatureItem(
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
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.secondaryText,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colorScheme.primaryText,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun BuildInfoFooter(
    versionName: String,
    versionCode: String,
    buildType: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Build Info",
                    modifier = Modifier.size(16.dp),
                    tint = AppTheme.colorScheme.secondaryText
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Build Info",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colorScheme.secondaryText,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "v$versionName ($versionCode)",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colorScheme.primaryText,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = buildType.uppercase(),
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colorScheme.secondaryText
                )
            }
        }
    }
}

// Preview Composables
@Preview(name = "About Screen Light")
@Composable
private fun AboutScreenLightPreview() {
    AppTheme(isDarkMode = false) {
        AboutScreenContent(
            appName = "MultiModule Movies",
            versionName = "1.0.0",
            versionCode = "1",
            buildType = "debug"
        )
    }
}

@Preview(name = "About Screen Dark")
@Composable
private fun AboutScreenDarkPreview() {
    AppTheme(isDarkMode = true) {
        AboutScreenContent(
            appName = "MultiModule Movies",
            versionName = "1.0.0",
            versionCode = "1",
            buildType = "release"
        )
    }
}

@Preview(name = "App Info Header")
@Composable
private fun AppInfoHeaderPreview() {
    AppTheme(isDarkMode = false) {
        AppInfoHeader(appName = "MultiModule Movies")
    }
}

@Preview(name = "Build Info Footer")
@Composable
private fun BuildInfoFooterPreview() {
    AppTheme(isDarkMode = false) {
        BuildInfoFooter(
            versionName = "1.0.0",
            versionCode = "1",
            buildType = "debug"
        )
    }
}