package com.alican.multimodulemovies.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieType
import com.alican.multimodulemovies.components.widget.CustomWidget
import com.alican.multimodulemovies.components.widget.MovieWidgetComponentModel
import com.alican.multimodulemovies.components.widget.toWidgetModel
import kotlinx.collections.immutable.adapters.ImmutableListAdapter

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    openListScreen: (type : MovieType) -> Unit
) {

    val upComingMovies by viewModel.upComingMovies.collectAsStateWithLifecycle()
    val nowPlayingMovies by viewModel.nowPlayingMovies.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {

        when (upComingMovies) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val movies = (upComingMovies as BaseUIModel.Success).data.map { it.toWidgetModel() }
                val widgetModel = MovieWidgetComponentModel(
                    title = "Upcoming",
                    items = ImmutableListAdapter(movies)
                )
                CustomWidget(model = widgetModel, openListScreen = {
                    openListScreen.invoke(MovieType.UPCOMING)
                })
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
                CustomWidget(model = widgetModel,openListScreen = {
                    openListScreen.invoke(MovieType.NOW_PLAYING)
                })
            }

        }
    }
}