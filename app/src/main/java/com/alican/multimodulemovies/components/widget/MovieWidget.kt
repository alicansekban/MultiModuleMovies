package com.alican.multimodulemovies.components.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alican.multimodulemovies.utils.heightPercent
import com.alican.multimodulemovies.utils.widthPercent
import com.alican.multimodulemovies.components.imageView.CustomImageViewWithLoading

@Composable
fun CustomWidget(
    model: MovieWidgetComponentModel,
    openListScreen: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { openListScreen() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            model.title?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it,
                )
            }
            Text(
                text = "View all",
                textDecoration = TextDecoration.Underline,
            )
        }
        LazyRow(Modifier.fillMaxWidth(), contentPadding = PaddingValues(start = 16.dp)) {
            items(model.items) {
                WidgetItem(item = it)
            }
        }
    }
}

@Composable
fun WidgetItem(item: WidgetMovieModel) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
            }
            .padding(8.dp)
            .widthPercent(0.35f, configuration),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        CustomImageViewWithLoading(
            imageUrl = item.imageUrl ?: "",
            modifier = Modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .aspectRatio(4f / 6f)
                .heightPercent(0.35f, configuration),
        )

        item.title?.let {
            Text(
                text = it,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}