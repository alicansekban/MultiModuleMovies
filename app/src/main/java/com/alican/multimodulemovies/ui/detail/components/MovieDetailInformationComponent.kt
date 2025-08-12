package com.alican.multimodulemovies.ui.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alican.domain.models.MovieDetailUIModel
import com.alican.multimodulemovies.theme.AppTheme

@Composable
fun MovieDetailInformation(movie: MovieDetailUIModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            movie.title?.let {
                Text(
                    text = it,
                    style = AppTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colorScheme.primaryText
                )
            }

            // Movie Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Rating
                movie.voteAvg?.let { rating ->
                    MovieStatItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = AppTheme.colorScheme.warningColor
                            )
                        },
                        text = rating,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Release Date
                movie.releaseDate?.let { date ->
                    MovieStatItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Release Date",
                                tint = AppTheme.colorScheme.primaryButton
                            )
                        },
                        text = date,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Duration
                movie.duration?.let { duration ->
                    MovieStatItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Duration",
                                tint = AppTheme.colorScheme.successColor
                            )
                        },
                        text = duration,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Overview
            movie.overview?.let { overview ->
                Column {
                    Text(
                        text = "Overview",
                        style = AppTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colorScheme.primaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = overview,
                        style = AppTheme.typography.bodyLarge,
                        color = AppTheme.colorScheme.secondaryText,
                        lineHeight = AppTheme.typography.bodyLarge.lineHeight
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieStatItem(
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardSecondaryBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = AppTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colorScheme.primaryText
            )
        }
    }
}