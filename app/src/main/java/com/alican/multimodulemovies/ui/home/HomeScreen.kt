package com.alican.multimodulemovies.ui.home

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

    val upComingMovies by viewModel.upComingMovies.collectAsStateWithLifecycle()
    val nowPlayingMovies by viewModel.nowPlayingMovies.collectAsStateWithLifecycle()
    val topRatedMovies by viewModel.topRatedMovies.collectAsStateWithLifecycle()
    val popularMovies by viewModel.popularMovies.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Welcome Section
        WelcomeSection()

        when (upComingMovies) {
            BaseUIModel.Empty -> EmptyStateCard("No upcoming movies available")
            is BaseUIModel.Error -> {
                val errorMessage = (upComingMovies as BaseUIModel.Error).message
                ErrorStateCard(errorMessage)
            }

            BaseUIModel.Loading -> LoadingStateCard()
            is BaseUIModel.Success -> {
                val movies = (upComingMovies as BaseUIModel.Success).data
                val widgetMovies = movies.map { it.toWidgetModel() }
                val widgetModel = MovieWidgetComponentModel(
                    title = "Upcoming",
                    items = widgetMovies
                )

                // Hero Pager Section
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
                        onClick = {
                            val movie = movies[it]
                            movie.id?.let { movieId -> openMovieDetailScreen.invoke(movieId) }
                        }
                    )
                }

                CustomWidget(
                    model = widgetModel,
                    openListScreen = { openListScreen.invoke(MovieType.UPCOMING) },
                    openMovieDetailScreen = openMovieDetailScreen
                )
            }
        }

        when (nowPlayingMovies) {
            BaseUIModel.Empty -> EmptyStateCard("No movies playing now")
            is BaseUIModel.Error -> {
                val errorMessage = (nowPlayingMovies as BaseUIModel.Error).message
                ErrorStateCard(errorMessage)
            }

            BaseUIModel.Loading -> LoadingStateCard()
            is BaseUIModel.Success -> {
                val movies =
                    (nowPlayingMovies as BaseUIModel.Success<List<MovieUIModel>>).data.map { it.toWidgetModel() }
                val widgetModel = MovieWidgetComponentModel(
                    title = "Now Playing",
                    items = movies
                )
                CustomWidget(
                    model = widgetModel,
                    openListScreen = { openListScreen.invoke(MovieType.NOW_PLAYING) },
                    openMovieDetailScreen = openMovieDetailScreen
                )
            }
        }

        when (topRatedMovies) {
            BaseUIModel.Empty -> EmptyStateCard("No top rated movies available")
            is BaseUIModel.Error -> {
                val errorMessage = (topRatedMovies as BaseUIModel.Error).message
                ErrorStateCard(errorMessage)
            }

            BaseUIModel.Loading -> LoadingStateCard()
            is BaseUIModel.Success -> {
                val movies =
                    (topRatedMovies as BaseUIModel.Success<List<MovieUIModel>>).data.map { it.toWidgetModel() }
                val widgetModel = MovieWidgetComponentModel(
                    title = "Top Rated",
                    items = movies
                )
                CustomWidget(
                    model = widgetModel,
                    openListScreen = { openListScreen.invoke(MovieType.TOP_RATED) },
                    openMovieDetailScreen = openMovieDetailScreen
                )
            }
        }

        when (popularMovies) {
            BaseUIModel.Empty -> EmptyStateCard("No popular movies available")
            is BaseUIModel.Error -> {
                val errorMessage = (popularMovies as BaseUIModel.Error).message
                ErrorStateCard(errorMessage)
            }

            BaseUIModel.Loading -> LoadingStateCard()
            is BaseUIModel.Success -> {
                val movies =
                    (popularMovies as BaseUIModel.Success<List<MovieUIModel>>).data.map { it.toWidgetModel() }
                val widgetModel = MovieWidgetComponentModel(
                    title = "Popular",
                    items = movies
                )
                CustomWidget(
                    model = widgetModel,
                    openListScreen = { openListScreen.invoke(MovieType.POPULAR) },
                    openMovieDetailScreen = openMovieDetailScreen
                )
            }
        }

        // Bottom spacing
        Spacer(modifier = Modifier.height(16.dp))
    }
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
private fun ErrorStateCard(message: String) {
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