package com.alican.multimodulemovies.ui.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alican.domain.models.MovieDetailUIModel

@Composable
fun MovieDetailInformation(movie: MovieDetailUIModel) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        movie.title?.let { Text(modifier = Modifier, text = it) }
        movie.overview?.let { Text(modifier = Modifier, text = it) }
        movie.releaseDate?.let { Text(modifier = Modifier, text = it) }
        movie.voteAvg?.let { Text(modifier = Modifier, text = it) }
        movie.duration?.let {
            Text(modifier = Modifier, text = it)
        }
    }
}