package com.alican.multimodulemovies.theme

import androidx.compose.ui.graphics.Color


data class AppColorScheme(
    val primaryBackground: Color = Color.Unspecified,
    val primaryText: Color = Color.Unspecified,
    val primaryButton: Color = Color.Unspecified,
    val secondaryBackground: Color = Color.Unspecified,
    val secondaryText: Color = Color.Unspecified,
    val secondaryButton: Color = Color.Unspecified,
)

internal val LightColorScheme = AppColorScheme(
    primaryBackground = Color(0xFFFFFFFF), // White
    primaryText = Color(0xFF1C1C1E), // Dark gray/black
    primaryButton = Color(0xFF007AFF), // Blue
    secondaryBackground = Color(0xFFF2F2F7), // Light gray
    secondaryText = Color(0xFF6D6D70), // Medium gray
    secondaryButton = Color(0xFF34C759), // Green
)

internal val DarkColorScheme = AppColorScheme(
    primaryBackground = Color(0xFF000000), // Black
    primaryText = Color(0xFFFFFFFF), // White
    primaryButton = Color(0xFF0A84FF), // Lighter blue
    secondaryBackground = Color(0xFF1C1C1E), // Dark gray
    secondaryText = Color(0xFF98989D), // Light gray
    secondaryButton = Color(0xFF30D158), // Lighter green
)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Orange = Color(0xFFef5e07)