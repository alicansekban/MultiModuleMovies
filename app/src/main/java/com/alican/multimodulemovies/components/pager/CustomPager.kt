package com.alican.multimodulemovies.components.pager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alican.multimodulemovies.components.imageView.CustomImageView
import com.app.sefamerve.components.dot_indicator.PagerIndicator
import kotlinx.coroutines.launch

@Composable
fun CustomPager(
    modifier: Modifier = Modifier,
    images: List<String>,
    onClick: (index: Int) -> Unit

) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { images.size }
    )
    Box(
        modifier = modifier.clickable {
            onClick.invoke(pagerState.currentPage)
        },
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            CustomImageView(
                imageUrl = images[page], modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
            )
        }
        if (images.size > 1) {
            PagerIndicator(
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                coroutineScope.launch {
                    pagerState.scrollToPage(it)
                }
            }
        }
    }
}


