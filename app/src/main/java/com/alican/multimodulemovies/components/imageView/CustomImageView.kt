package com.alican.multimodulemovies.components.imageView

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.util.DebugLogger
import com.alican.multimodulemovies.R

@Composable
fun CustomImageView(
    imageUrl: String,
    modifier: Modifier,
    @DrawableRes errorResource: Int = R.drawable.ic_launcher_background,
    contentScale: ContentScale = ContentScale.FillBounds,
    colorFilter: ColorFilter? = null,
    onClick : () -> Unit = {}
) {
    val imageLoader = LocalContext.current.imageLoader.newBuilder()
        .logger(DebugLogger())
        .respectCacheHeaders(false)
        .build()
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .placeholder(errorResource)
            .error(errorResource)
            .fallback(errorResource)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .allowHardware(true)
            .build(),
        imageLoader = imageLoader,
        contentDescription = null,
        modifier = modifier,
        placeholder = painterResource(id = errorResource),
        error = painterResource(id = errorResource),
        contentScale = contentScale,
        colorFilter = colorFilter
    )
}

@Composable
fun CustomImageViewWithLoading(
    imageUrl: String,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes errorResource: Int = R.drawable.ic_launcher_background,
) {
    val imageLoader = LocalContext.current.imageLoader.newBuilder()
        .logger(DebugLogger())
        .respectCacheHeaders(false)
        .build()
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .allowHardware(true)
            .build(),
        imageLoader = imageLoader,
        contentDescription = "",
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        modifier = modifier,
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF6F6F9)),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(id = errorResource), contentDescription = "error")
            }
        },
        contentScale = contentScale
    )

}

