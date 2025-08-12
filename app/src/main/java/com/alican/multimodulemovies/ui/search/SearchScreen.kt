package com.alican.multimodulemovies.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alican.multimodulemovies.theme.AppTheme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Search Icon
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colorScheme.primaryButton.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(50.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .size(80.dp)
                    .padding(20.dp),
                tint = AppTheme.colorScheme.primaryButton
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Title
        Text(
            text = "Search Movies",
            style = AppTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colorScheme.primaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtitle
        Text(
            text = "Search functionality will be available soon.\nFind your favorite movies, actors, and genres.",
            style = AppTheme.typography.bodyLarge,
            color = AppTheme.colorScheme.secondaryText,
            textAlign = TextAlign.Center,
            lineHeight = AppTheme.typography.bodyLarge.lineHeight
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Coming Soon Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colorScheme.cardSecondaryBackground
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Coming Soon",
                    style = AppTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colorScheme.accent
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Advanced search with filters, voice search, and personalized recommendations",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.secondaryText,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}