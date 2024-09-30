package com.alican.multimodulemovies.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieType
import com.alican.multimodulemovies.components.pager.CustomPager
import com.alican.multimodulemovies.components.widget.CustomWidget
import com.alican.multimodulemovies.components.widget.MovieWidgetComponentModel
import com.alican.multimodulemovies.components.widget.toWidgetModel
import com.alican.multimodulemovies.utils.heightPercent
import kotlinx.collections.immutable.adapters.ImmutableListAdapter

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
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
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {


        when (upComingMovies) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val movies = (upComingMovies as BaseUIModel.Success).data
                val widgetMovies = movies.map { it.toWidgetModel() }
                val widgetModel = MovieWidgetComponentModel(
                    title = "Upcoming",
                    items = ImmutableListAdapter(widgetMovies)
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)) {
                    CustomPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightPercent(0.6f, configuration),
                        images = movies.map { it.imageUrl ?: "" },
                        onClick = {
                            openListScreen.invoke(MovieType.UPCOMING)
                        }
                    )
                }
                CustomWidget(model = widgetModel, openListScreen = {
                    openListScreen.invoke(MovieType.UPCOMING)
                },openMovieDetailScreen = openMovieDetailScreen)
            }
        }

        when (nowPlayingMovies) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val movies =
                    (nowPlayingMovies as BaseUIModel.Success).data.map { it.toWidgetModel() }
                val widgetModel = MovieWidgetComponentModel(
                    title = "Now Playing",
                    items = ImmutableListAdapter(movies)
                )
                CustomWidget(model = widgetModel, openListScreen = {
                    openListScreen.invoke(MovieType.NOW_PLAYING)
                },openMovieDetailScreen = openMovieDetailScreen)
            }
        }

        when (topRatedMovies) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val movies =
                    (topRatedMovies as BaseUIModel.Success).data.map { it.toWidgetModel() }
                val widgetModel = MovieWidgetComponentModel(
                    title = "Top Rated",
                    items = ImmutableListAdapter(movies)
                )
                CustomWidget(model = widgetModel, openListScreen = {
                    openListScreen.invoke(MovieType.TOP_RATED)
                }, openMovieDetailScreen = openMovieDetailScreen)
            }
        }

        when (popularMovies) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val movies =
                    (popularMovies as BaseUIModel.Success).data.map { it.toWidgetModel() }
                val widgetModel = MovieWidgetComponentModel(
                    title = "Popular",
                    items = ImmutableListAdapter(movies)
                )
                CustomWidget(model = widgetModel, openListScreen = {
                    openListScreen.invoke(MovieType.POPULAR)
                },openMovieDetailScreen = openMovieDetailScreen)
            }
        }
    }
}