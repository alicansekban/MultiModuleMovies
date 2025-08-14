package com.alican.multimodulemovies.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf

internal val LocalAppColorScheme = compositionLocalOf<AppColorScheme> {
    error("No ColorScheme provided")
}

internal val LocalAppTypography = compositionLocalOf<Typography> {
    error("No Typography provided")
}


object AppTheme {
    val colorScheme: AppColorScheme
        @Composable get() = LocalAppColorScheme.current
    val typography: Typography
        @Composable get() = LocalAppTypography.current
}

@Composable
fun AppTheme(
    isDarkMode: Boolean = false,
    content: @Composable () -> Unit
) {

    val providedValues = arrayListOf<ProvidedValue<*>>()
    providedValues += LocalAppColorScheme provides if (isDarkMode) DarkColorScheme else LightColorScheme
    providedValues += LocalAppTypography provides AppTypography

    CompositionLocalProvider(
        values = providedValues.toTypedArray(),
        content = content
    )
}