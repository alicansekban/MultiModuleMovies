package com.alican.multimodulemovies.ui.home.components


import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alican.domain.models.MovieType
import com.alican.domain.models.MovieUIModel
import com.alican.multimodulemovies.components.pager.CustomPager
import com.alican.multimodulemovies.components.widget.CustomWidget
import com.alican.multimodulemovies.components.widget.MovieWidgetComponentModel
import com.alican.multimodulemovies.components.widget.toWidgetModel
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.utils.heightPercent

@Composable
fun MovieSection(
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
