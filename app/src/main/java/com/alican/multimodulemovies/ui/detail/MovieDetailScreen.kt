package com.alican.multimodulemovies.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.models.MovieCreditsUIModel
import com.alican.domain.models.MovieDetailUIModel
import com.alican.domain.models.MovieReviewsUIModel
import com.alican.multimodulemovies.components.pager.CustomPager
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.ui.detail.components.MovieDetailInformation
import com.alican.multimodulemovies.utils.heightPercent


@Composable
fun MovieDetailScreen(viewmodel: MovieDetailViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Show loading state
        if (uiState.isLoading) {
            LoadingSection()
            return@Column
        }

        // Movie Images Section
        MovieImagesSection(
            images = uiState.movieImages,
            configuration = configuration
        )

        // Movie Details Section
        MovieDetailSection(movieDetail = uiState.movieDetail)

        // Movie Credits Section
        MovieCreditsSection(credits = uiState.movieCredits)

        // Movie Reviews Section
        MovieReviewsSection(reviews = uiState.movieReviews)

        // Bottom spacing
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MovieImagesSection(
    images: List<String>,
    configuration: Configuration
) {
    if (images.isEmpty()) {
        EmptyImageSection()
        return
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        CustomPager(
            images = images,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .heightPercent(0.6f, configuration)
        )
    }
}

@Composable
private fun MovieDetailSection(movieDetail: MovieDetailUIModel?) {
    if (movieDetail == null) {
        EmptySection("No movie details available")
        return
    }

    MovieDetailInformation(movie = movieDetail)
}

@Composable
private fun MovieCreditsSection(credits: List<MovieCreditsUIModel>) {
    if (credits.isEmpty()) {
        EmptySection("No cast information available")
        return
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Cast & Crew",
                style = AppTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText
            )
            Spacer(modifier = Modifier.height(12.dp))

            credits.take(5).forEach { credit ->
                Text(
                    text = "${credit.name} as ${credit.characterName}",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.secondaryText,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            if (credits.size > 5) {
                Text(
                    text = "and ${credits.size - 5} more...",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colorScheme.accent,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun MovieReviewsSection(reviews: List<MovieReviewsUIModel>) {
    if (reviews.isEmpty()) {
        EmptySection("No reviews available")
        return
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Reviews (${reviews.size})",
                style = AppTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText
            )
            Spacer(modifier = Modifier.height(12.dp))

            reviews.take(3).forEach { review ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.colorScheme.cardSecondaryBackground
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = review.author ?: "Anonymous",
                            style = AppTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colorScheme.primaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = review.content?.take(150) + if ((review.content?.length
                                    ?: 0) > 150
                            ) "..." else "",
                            style = AppTheme.typography.bodySmall,
                            color = AppTheme.colorScheme.secondaryText,
                            maxLines = 3
                        )
                    }
                }
            }

            if (reviews.size > 3) {
                Text(
                    text = "View ${reviews.size - 3} more reviews",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colorScheme.accent,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LoadingSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = AppTheme.colorScheme.primaryButton
            )
        }
    }
}

@Composable
private fun EmptySection(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                text = "No Content",
                style = AppTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.secondaryText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.secondaryText,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EmptyImageSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardSecondaryBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Images Available",
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colorScheme.secondaryText
            )
        }
    }
}