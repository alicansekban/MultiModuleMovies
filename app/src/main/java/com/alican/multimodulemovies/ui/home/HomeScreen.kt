package com.alican.multimodulemovies.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.models.MovieType
import com.alican.domain.models.MovieUIModel
import com.alican.multimodulemovies.components.card.LoadingStateCard
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.ui.home.components.MovieSection
import com.alican.multimodulemovies.ui.home.components.WelcomeSection


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
    ) {
        HomeScreenContent(
            uiState = uiState,
            onEvent = viewModel::onScreenEvent
        )

    }
}


@Composable
fun HomeScreenContent(
    uiState: HomeUIState,
    onEvent: (HomeUIEvents) -> Unit = {}
) {
    val configuration = LocalConfiguration.current

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
            openListScreen = { onEvent(HomeUIEvents.OpenMovieList(it)) },
            openMovieDetailScreen = { onEvent(HomeUIEvents.OpenMovieDetail(it)) }
        )

        // Now Playing Section
        MovieSection(
            movies = uiState.nowPlayingMovies,
            title = "Now Playing",
            movieType = MovieType.NOW_PLAYING,
            showPager = false,
            configuration = configuration,
            openListScreen = { onEvent(HomeUIEvents.OpenMovieList(it)) },
            openMovieDetailScreen = { onEvent(HomeUIEvents.OpenMovieDetail(it)) }
        )

        // Top Rated Section
        MovieSection(
            movies = uiState.topRatedMovies,
            title = "Top Rated",
            movieType = MovieType.TOP_RATED,
            showPager = false,
            configuration = configuration,
            openListScreen = { onEvent(HomeUIEvents.OpenMovieList(it)) },
            openMovieDetailScreen = { onEvent(HomeUIEvents.OpenMovieDetail(it)) }
        )

        // Popular Section
        MovieSection(
            movies = uiState.popularMovies,
            title = "Popular",
            movieType = MovieType.POPULAR,
            showPager = false,
            configuration = configuration,
            openListScreen = { onEvent(HomeUIEvents.OpenMovieList(it)) },
            openMovieDetailScreen = { onEvent(HomeUIEvents.OpenMovieDetail(it)) }
        )

        // Bottom spacing
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(name = "Home Screen Light - Loading")
@Composable
private fun HomeScreenLoadingPreview() {
    AppTheme(isDarkMode = false) {
        HomeScreenContent(
            uiState = HomeUIState(
                upcomingMovies = emptyList(),
                nowPlayingMovies = emptyList(),
                popularMovies = emptyList(),
                topRatedMovies = emptyList(),
                isLoading = true
            )
        )
    }
}

@Preview(name = "Home Screen Dark - Loading")
@Composable
private fun HomeScreenLoadingDarkPreview() {
    AppTheme(isDarkMode = true) {
        HomeScreenContent(
            uiState = HomeUIState(
                upcomingMovies = emptyList(),
                nowPlayingMovies = emptyList(),
                popularMovies = emptyList(),
                topRatedMovies = emptyList(),
                isLoading = true
            )
        )
    }
}

@Preview(name = "Home Screen Light - Empty")
@Composable
private fun HomeScreenEmptyPreview() {
    AppTheme(isDarkMode = false) {
        HomeScreenContent(
            uiState = HomeUIState(
                upcomingMovies = emptyList(),
                nowPlayingMovies = emptyList(),
                popularMovies = emptyList(),
                topRatedMovies = emptyList(),
                isLoading = false
            )
        )
    }
}

@Preview(name = "Home Screen Dark - Empty")
@Composable
private fun HomeScreenEmptyDarkPreview() {
    AppTheme(isDarkMode = true) {
        HomeScreenContent(
            uiState = HomeUIState(
                upcomingMovies = emptyList(),
                nowPlayingMovies = emptyList(),
                popularMovies = emptyList(),
                topRatedMovies = emptyList(),
                isLoading = false
            )
        )
    }
}

@Preview(name = "Home Screen Light - With Data")
@Composable
private fun HomeScreenWithDataPreview() {
    AppTheme(isDarkMode = false) {
        val sampleMovies = listOf(
            MovieUIModel(
                id = 1,
                title = "The Amazing Spider-Man",
                overview = "A great superhero movie",
            ),
            MovieUIModel(
                id = 2,
                title = "Inception",
                overview = "A mind-bending thriller",
            ),
            MovieUIModel(
                id = 3,
                title = "The Dark Knight",
                overview = "Batman's greatest challenge",
            )
        )

        HomeScreenContent(
            uiState = HomeUIState(
                upcomingMovies = sampleMovies,
                nowPlayingMovies = sampleMovies.take(2),
                popularMovies = sampleMovies.reversed(),
                topRatedMovies = sampleMovies.drop(1),
                isLoading = false
            )
        )
    }
}

@Preview(name = "Home Screen Dark - With Data")
@Composable
private fun HomeScreenWithDataDarkPreview() {
    AppTheme(isDarkMode = true) {
        val sampleMovies = listOf(
            MovieUIModel(
                id = 1,
                title = "The Amazing Spider-Man",
                overview = "A great superhero movie",
            ),
            MovieUIModel(
                id = 2,
                title = "Inception",
                overview = "A mind-bending thriller",
            )
        )

        HomeScreenContent(
            uiState = HomeUIState(
                upcomingMovies = sampleMovies,
                nowPlayingMovies = sampleMovies.take(2),
                popularMovies = sampleMovies.reversed(),
                topRatedMovies = sampleMovies.drop(1),
                isLoading = false
            )
        )
    }
}
