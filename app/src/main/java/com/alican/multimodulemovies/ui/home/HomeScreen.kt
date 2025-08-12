package com.alican.multimodulemovies.ui.home

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
import com.alican.domain.models.MovieType
import com.alican.domain.models.MovieUIModel
import com.alican.multimodulemovies.components.pager.CustomPager
import com.alican.multimodulemovies.components.widget.CustomWidget
import com.alican.multimodulemovies.components.widget.MovieWidgetComponentModel
import com.alican.multimodulemovies.components.widget.toWidgetModel
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.utils.heightPercent


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    openListScreen: (type: MovieType) -> Unit,
    openMovieDetailScreen: (id: Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Welcome Section
        WelcomeSection()

        // Show loading state
        if (uiState.isLoading) {
            LoadingStateCard()
            return@Column
        }

        // Upcoming Movies Section
        MovieSection(
            movies = uiState.upcomingMovies,
            title = "Upcoming",
            movieType = MovieType.UPCOMING,
            showPager = true,
            configuration = configuration,
            openListScreen = openListScreen,
            openMovieDetailScreen = openMovieDetailScreen
        )

        // Now Playing Section
        MovieSection(
            movies = uiState.nowPlayingMovies,
            title = "Now Playing",
            movieType = MovieType.NOW_PLAYING,
            showPager = false,
            configuration = configuration,
            openListScreen = openListScreen,
            openMovieDetailScreen = openMovieDetailScreen
        )

        // Top Rated Section
        MovieSection(
            movies = uiState.topRatedMovies,
            title = "Top Rated",
            movieType = MovieType.TOP_RATED,
            showPager = false,
            configuration = configuration,
            openListScreen = openListScreen,
            openMovieDetailScreen = openMovieDetailScreen
        )

        // Popular Section
        MovieSection(
            movies = uiState.popularMovies,
            title = "Popular",
            movieType = MovieType.POPULAR,
            showPager = false,
            configuration = configuration,
            openListScreen = openListScreen,
            openMovieDetailScreen = openMovieDetailScreen
        )

        // Bottom spacing
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MovieSection(
    movies: List<MovieUIModel>,
    title: String,
    movieType: MovieType,
    showPager: Boolean,
    configuration: Configuration,
    openListScreen: (MovieType) -> Unit,
    openMovieDetailScreen: (Int) -> Unit
) {
    if (movies.isEmpty()) {
        EmptyStateCard("No $title movies available")
        return
    }

    val widgetMovies = movies.map { it.toWidgetModel() }
    val widgetModel = MovieWidgetComponentModel(
        title = title,
        items = widgetMovies
    )

    if (showPager) {
        // Hero Pager Section for Upcoming Movies
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colorScheme.cardBackground
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            CustomPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightPercent(0.6f, configuration),
                images = movies.map { it.imageUrl ?: "" },
                onClick = { index ->
                    val movie = movies[index]
                    movie.id?.let { movieId -> openMovieDetailScreen.invoke(movieId) }
                }
            )
        }
    }

    CustomWidget(
        model = widgetModel,
        openListScreen = { openListScreen.invoke(movieType) },
        openMovieDetailScreen = openMovieDetailScreen
    )
}

@Composable
private fun WelcomeSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardSecondaryBackground
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Movies",
                style = AppTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Discover the latest movies, trending shows, and more",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.secondaryText,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LoadingStateCard() {
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
private fun EmptyStateCard(message: String) {
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