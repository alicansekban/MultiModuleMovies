package com.alican.multimodulemovies.helpers.navigation3

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.alican.domain.ui_models.movie.MovieType
import kotlinx.serialization.Serializable

sealed class EntryRoutes : NavKey {
    @Serializable
    data class MoviesListRoute(
        val movieType: MovieType = MovieType.UPCOMING
    ) : EntryRoutes()

    @Serializable
    data class MovieDetailRoute(
        val movieId: Int = 123
    ) : EntryRoutes()

    @Serializable
    data object LoginEntryRoutes : EntryRoutes()

    @Serializable
    data object RegisterEntryRoutes : EntryRoutes()

    @Serializable
    data object AboutEntryRoutes : EntryRoutes()

    @Serializable
    data object HelpEntryRoutes : EntryRoutes()

    @Serializable
    data object PrivacyPolicyEntryRoutes : EntryRoutes()

    @Serializable
    data object SecurityEntryRoutes : EntryRoutes()
}

sealed interface BottomNavRoutes : NavKey {
    val title: String
    val icon: ImageVector

    @Serializable
    data object Home : BottomNavRoutes {
        override val title: String = "Home"
        override val icon: ImageVector = Icons.Filled.Home
    }

    @Serializable
    data object Search : BottomNavRoutes {
        override val title: String = "Search"
        override val icon: ImageVector = Icons.Filled.Search
    }

    @Serializable
    data object Favorites : BottomNavRoutes {
        override val title: String = "Favorites"
        override val icon: ImageVector = Icons.Filled.Favorite
    }

    @Serializable
    data object Profile : BottomNavRoutes {
        override val title: String = "Profile"
        override val icon: ImageVector = Icons.Filled.Man
    }
}