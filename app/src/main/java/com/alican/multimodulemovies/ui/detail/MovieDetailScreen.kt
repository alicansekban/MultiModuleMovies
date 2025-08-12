package com.alican.multimodulemovies.ui.detail

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
import com.alican.domain.models.BaseUIModel
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

    val movieDetail by viewmodel.movieDetail.collectAsStateWithLifecycle()
    val movieImages by viewmodel.movieImages.collectAsStateWithLifecycle()
    val movieCredits by viewmodel.movieCredits.collectAsStateWithLifecycle()
    val movieReviews by viewmodel.movieReviews.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Movie Images Pager
        when (movieImages) {
            BaseUIModel.Empty -> EmptyImageSection()
            is BaseUIModel.Error -> ErrorSection("Failed to load images: ${(movieImages as BaseUIModel.Error<List<String>>).message}")
            BaseUIModel.Loading -> LoadingSection()
            is BaseUIModel.Success -> {
                val images = (movieImages as BaseUIModel.Success<List<String>>).data
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
        }

        // Movie Details
        when (movieDetail) {
            BaseUIModel.Empty -> EmptySection("No movie details available")
            is BaseUIModel.Error -> ErrorSection("Failed to load details: ${(movieDetail as BaseUIModel.Error<MovieDetailUIModel>).message}")
            BaseUIModel.Loading -> LoadingSection()
            is BaseUIModel.Success -> {
                val movie = (movieDetail as BaseUIModel.Success<MovieDetailUIModel>).data
                MovieDetailInformation(movie = movie)
            }
        }

        // Movie Credits
        when (movieCredits) {
            BaseUIModel.Empty -> EmptySection("No cast information available")
            is BaseUIModel.Error -> ErrorSection("Failed to load cast: ${(movieCredits as BaseUIModel.Error<List<MovieCreditsUIModel>>).message}")
            BaseUIModel.Loading -> LoadingSection()
            is BaseUIModel.Success -> {
                val credits = (movieCredits as BaseUIModel.Success<List<MovieCreditsUIModel>>).data
                CastSection(credits)
            }
        }

        // Movie Reviews
        when (movieReviews) {
            BaseUIModel.Empty -> EmptySection("No reviews available")
            is BaseUIModel.Error -> ErrorSection("Failed to load reviews: ${(movieReviews as BaseUIModel.Error<List<MovieReviewsUIModel>>).message}")
            BaseUIModel.Loading -> LoadingSection()
            is BaseUIModel.Success -> {
                val reviews = (movieReviews as BaseUIModel.Success<List<MovieReviewsUIModel>>).data
                ReviewsSection(reviews)
            }
        }

        // Bottom spacing
        Spacer(modifier = Modifier.height(16.dp))
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
private fun ErrorSection(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.errorColor.copy(alpha = 0.1f)
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
                text = "Error",
                style = AppTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.errorColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.primaryText,
                textAlign = TextAlign.Center
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

@Composable
private fun CastSection(credits: Any) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
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
            Text(
                text = "Cast information will be displayed here",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.secondaryText
            )
        }
    }
}

@Composable
private fun ReviewsSection(reviews: Any) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Reviews",
                style = AppTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Movie reviews will be displayed here",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.secondaryText
            )
        }
    }
}