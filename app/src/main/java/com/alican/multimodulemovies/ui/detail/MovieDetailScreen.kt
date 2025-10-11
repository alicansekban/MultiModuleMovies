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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.ui_models.movie.MovieCreditsUIModel
import com.alican.domain.ui_models.movie.MovieDetailUIModel
import com.alican.domain.ui_models.movie.MovieReviewsUIModel
import com.alican.domain.ui_models.movie_detail.MovieDetailUIState
import com.alican.multimodulemovies.components.pager.CustomPager
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.ui.detail.components.MovieDetailInformation
import com.alican.multimodulemovies.utils.heightPercent

@Composable
fun MovieDetailScreen(viewModel: MovieDetailViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovieDetailScreenContent(
        uiState = uiState,
        configuration = configuration,
        onEvent = viewModel::onScreenEvent
    )
}

@Composable
fun MovieDetailScreenContent(
    uiState: MovieDetailUIState,
    configuration: Configuration,
    onEvent: (MovieDetailUIEvents) -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
        // Favorite Icon
        IconButton(
            onClick = {
                onEvent(MovieDetailUIEvents.ToggleFavorite)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(40.dp)
        ) {
            Icon(
                imageVector = if (uiState.movieDetail?.isFavorite == true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (uiState.movieDetail?.isFavorite == true) "Remove from favorites" else "Add to favorites",
                tint = if (uiState.movieDetail?.isFavorite == true) Color.Red else AppTheme.colorScheme.primaryText.copy(
                    alpha = 0.7f
                ),
                modifier = Modifier.size(24.dp)
            )
        }
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

// Preview Composables
@Preview(name = "Movie Detail Light - Loading")
@Composable
private fun MovieDetailLoadingPreview() {
    AppTheme(isDarkMode = false) {
        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = true,
                movieDetail = null,
                movieImages = emptyList(),
                movieCredits = emptyList(),
                movieReviews = emptyList()
            ),
            configuration = LocalConfiguration.current
        )
    }
}

@Preview(name = "Movie Detail Dark - Loading")
@Composable
private fun MovieDetailLoadingDarkPreview() {
    AppTheme(isDarkMode = true) {
        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = true,
                movieDetail = null,
                movieImages = emptyList(),
                movieCredits = emptyList(),
                movieReviews = emptyList()
            ),
            configuration = LocalConfiguration.current
        )
    }
}

@Preview(name = "Movie Detail Light - With Data")
@Composable
private fun MovieDetailWithDataPreview() {
    AppTheme(isDarkMode = false) {
        val sampleMovieDetail = MovieDetailUIModel(
            id = 1,
            title = "The Amazing Spider-Man",
            overview = "After Peter Parker is bitten by a genetically altered spider, he gains newfound, spider-like powers and ventures out to save the city from the machinations of a mysterious reptilian foe.",
            releaseDate = "2024-01-15",
            voteAvg = "8.5",
            duration = "136 min",
            imageUrl = "/sample_poster.jpg"
        )

        val sampleCredits = listOf(
            MovieCreditsUIModel(
                name = "Tom Holland",
                characterName = "Peter Parker / Spider-Man"
            ),
            MovieCreditsUIModel(
                name = "Zendaya",
                characterName = "MJ"
            ),
            MovieCreditsUIModel(
                name = "Benedict Cumberbatch",
                characterName = "Doctor Strange"
            ),
            MovieCreditsUIModel(
                name = "Jacob Batalon",
                characterName = "Ned Leeds"
            ),
            MovieCreditsUIModel(
                name = "Marisa Tomei",
                characterName = "Aunt May"
            ),
            MovieCreditsUIModel(
                name = "Jon Favreau",
                characterName = "Happy Hogan"
            )
        )

        val sampleReviews = listOf(
            MovieReviewsUIModel(
                author = "John Doe",
                content = "An amazing superhero movie with great action sequences and excellent character development. Tom Holland delivers a fantastic performance as Spider-Man, bringing both humor and heart to the role. The visual effects are stunning and the story keeps you engaged throughout."
            ),
            MovieReviewsUIModel(
                author = "Jane Smith",
                content = "Visually stunning with incredible special effects. The story is engaging and the cast chemistry is perfect. This movie sets a new standard for superhero films."
            ),
            MovieReviewsUIModel(
                author = "Mike Johnson",
                content = "Great movie! Really enjoyed the character development and the action scenes. Highly recommended for all Marvel fans."
            ),
            MovieReviewsUIModel(
                author = "Sarah Wilson",
                content = "Excellent cinematography and outstanding performances from the entire cast. The movie balances action and emotion perfectly."
            )
        )

        val sampleImages = listOf(
            "/backdrop1.jpg",
            "/backdrop2.jpg",
            "/poster1.jpg",
            "/poster2.jpg",
            "/still1.jpg"
        )

        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = false,
                movieDetail = sampleMovieDetail,
                movieImages = sampleImages,
                movieCredits = sampleCredits,
                movieReviews = sampleReviews
            ),
            configuration = LocalConfiguration.current
        )
    }
}

@Preview(name = "Movie Detail Dark - With Data")
@Composable
private fun MovieDetailWithDataDarkPreview() {
    AppTheme(isDarkMode = true) {
        val sampleMovieDetail = MovieDetailUIModel(
            id = 2,
            title = "Inception",
            overview = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
            releaseDate = "2024-02-20",
            voteAvg = "9.0",
            duration = "148 min",
            imageUrl = "/inception_poster.jpg"
        )

        val sampleCredits = listOf(
            MovieCreditsUIModel(
                name = "Leonardo DiCaprio",
                characterName = "Dom Cobb"
            ),
            MovieCreditsUIModel(
                name = "Marion Cotillard",
                characterName = "Mal"
            ),
            MovieCreditsUIModel(
                name = "Tom Hardy",
                characterName = "Eames"
            )
        )

        val sampleReviews = listOf(
            MovieReviewsUIModel(
                author = "Cinema Critic",
                content = "Christopher Nolan's masterpiece. A complex and brilliantly executed film that challenges the audience while delivering spectacular visuals and performances."
            )
        )

        val sampleImages = listOf(
            "/inception_backdrop1.jpg",
            "/inception_poster1.jpg"
        )

        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = false,
                movieDetail = sampleMovieDetail,
                movieImages = sampleImages,
                movieCredits = sampleCredits,
                movieReviews = sampleReviews
            ),
            configuration = LocalConfiguration.current
        )
    }
}

@Preview(name = "Movie Detail Light - Minimal Data")
@Composable
private fun MovieDetailMinimalDataPreview() {
    AppTheme(isDarkMode = false) {
        val sampleMovieDetail = MovieDetailUIModel(
            id = 3,
            title = "Sample Movie",
            overview = "A short overview of this sample movie.",
            releaseDate = "2024-03-10",
            voteAvg = "7.2",
            duration = "90 min",
            imageUrl = "/sample.jpg"
        )

        val sampleCredits = listOf(
            MovieCreditsUIModel(
                name = "Actor One",
                characterName = "Main Character"
            ),
            MovieCreditsUIModel(
                name = "Actor Two",
                characterName = "Supporting Character"
            )
        )

        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = false,
                movieDetail = sampleMovieDetail,
                movieImages = listOf("/sample.jpg"),
                movieCredits = sampleCredits,
                movieReviews = emptyList()
            ),
            configuration = LocalConfiguration.current
        )
    }
}

@Preview(name = "Movie Detail Dark - Minimal Data")
@Composable
private fun MovieDetailMinimalDataDarkPreview() {
    AppTheme(isDarkMode = true) {
        val sampleMovieDetail = MovieDetailUIModel(
            id = 4,
            title = "Another Sample",
            releaseDate = "2024-04-05",
            voteAvg = "6.8",
            duration = null
        )

        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = false,
                movieDetail = sampleMovieDetail,
                movieImages = listOf("/backdrop_only.jpg"),
                movieCredits = emptyList(),
                movieReviews = emptyList()
            ),
            configuration = LocalConfiguration.current
        )
    }
}

@Preview(name = "Movie Detail Light - Empty Images")
@Composable
private fun MovieDetailEmptyImagesPreview() {
    AppTheme(isDarkMode = false) {
        val sampleMovieDetail = MovieDetailUIModel(
            id = 5,
            title = "Movie Without Images",
            overview = "This movie doesn't have any images available.",
            releaseDate = "2024-05-15",
            voteAvg = "7.8",
            duration = "120 min",
        )

        val sampleCredits = listOf(
            MovieCreditsUIModel(
                name = "Famous Actor",
                characterName = "Lead Role"
            )
        )

        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = false,
                movieDetail = sampleMovieDetail,
                movieImages = emptyList(),
                movieCredits = sampleCredits,
                movieReviews = emptyList()
            ),
            configuration = LocalConfiguration.current
        )
    }
}

@Preview(name = "Movie Detail Dark - Empty Images")
@Composable
private fun MovieDetailEmptyImagesDarkPreview() {
    AppTheme(isDarkMode = true) {
        val sampleMovieDetail = MovieDetailUIModel(
            id = 6,
            title = "Dark Movie Without Images",
            overview = "This is a dark theme preview without images.",
            releaseDate = "2024-06-20",
            voteAvg = "8.2",
            duration = "95 min",
        )

        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = false,
                movieDetail = sampleMovieDetail,
                movieImages = emptyList(),
                movieCredits = emptyList(),
                movieReviews = emptyList()
            ),
            configuration = LocalConfiguration.current
        )
    }
}

@Preview(name = "Movie Detail Light - No Movie Data")
@Composable
private fun MovieDetailNoDataPreview() {
    AppTheme(isDarkMode = false) {
        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = false,
                movieDetail = null,
                movieImages = emptyList(),
                movieCredits = emptyList(),
                movieReviews = emptyList()
            ),
            configuration = LocalConfiguration.current
        )
    }
}

@Preview(name = "Movie Detail Dark - No Movie Data")
@Composable
private fun MovieDetailNoDataDarkPreview() {
    AppTheme(isDarkMode = true) {
        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = false,
                movieDetail = null,
                movieImages = emptyList(),
                movieCredits = emptyList(),
                movieReviews = emptyList()
            ),
            configuration = LocalConfiguration.current
        )
    }
}

@Preview(name = "Movie Detail Light - Many Credits")
@Composable
private fun MovieDetailManyCreditsPreview() {
    AppTheme(isDarkMode = false) {
        val sampleMovieDetail = MovieDetailUIModel(
            id = 7,
            title = "Avengers: Endgame",
            overview = "The epic conclusion to the Infinity Saga that became the highest-grossing film of all time.",
            releaseDate = "2019-04-26",
            voteAvg = "9.2",
            duration = "181 min",
            imageUrl = "/avengers_poster.jpg"
        )

        val sampleCredits = listOf(
            MovieCreditsUIModel(
                name = "Robert Downey Jr.",
                characterName = "Tony Stark / Iron Man"
            ),
            MovieCreditsUIModel(
                name = "Chris Evans",
                characterName = "Steve Rogers / Captain America"
            ),
            MovieCreditsUIModel(name = "Mark Ruffalo", characterName = "Bruce Banner / Hulk"),
            MovieCreditsUIModel(name = "Chris Hemsworth", characterName = "Thor"),
            MovieCreditsUIModel(
                name = "Scarlett Johansson",
                characterName = "Natasha Romanoff / Black Widow"
            ),
            MovieCreditsUIModel(name = "Jeremy Renner", characterName = "Clint Barton / Hawkeye"),
            MovieCreditsUIModel(name = "Don Cheadle", characterName = "James Rhodes / War Machine"),
            MovieCreditsUIModel(name = "Paul Rudd", characterName = "Scott Lang / Ant-Man"),
            MovieCreditsUIModel(
                name = "Brie Larson",
                characterName = "Carol Danvers / Captain Marvel"
            ),
            MovieCreditsUIModel(name = "Karen Gillan", characterName = "Nebula")
        )

        MovieDetailScreenContent(
            uiState = MovieDetailUIState(
                isLoading = false,
                movieDetail = sampleMovieDetail,
                movieImages = listOf("/avengers_backdrop1.jpg", "/avengers_poster1.jpg"),
                movieCredits = sampleCredits,
                movieReviews = emptyList()
            ),
            configuration = LocalConfiguration.current
        )
    }
}