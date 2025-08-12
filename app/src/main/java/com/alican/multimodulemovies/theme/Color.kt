package com.alican.multimodulemovies.theme

import androidx.compose.ui.graphics.Color

data class AppColorScheme(
    val primaryBackground: Color = Color.Unspecified,
    val primaryText: Color = Color.Unspecified,
    val primaryButton: Color = Color.Unspecified,
    val secondaryBackground: Color = Color.Unspecified,
    val secondaryText: Color = Color.Unspecified,
    val secondaryButton: Color = Color.Unspecified,
    val cardBackground: Color = Color.Unspecified,
    val cardSecondaryBackground: Color = Color.Unspecified,
    val accent: Color = Color.Unspecified,
    val successColor: Color = Color.Unspecified,
    val errorColor: Color = Color.Unspecified,
    val warningColor: Color = Color.Unspecified,
    val statusOnline: Color = Color.Unspecified,
    val statusOffline: Color = Color.Unspecified,
    val divider: Color = Color.Unspecified,
)

internal val LightColorScheme = AppColorScheme(
    primaryBackground = Color(0xFFFFFFFF), // White
    primaryText = Color(0xFF1C1C1E), // Dark gray/black
    primaryButton = Color(0xFF007AFF), // Blue
    secondaryBackground = Color(0xFFF2F2F7), // Light gray
    secondaryText = Color(0xFF6D6D70), // Medium gray
    secondaryButton = Color(0xFF34C759), // Green
    cardBackground = Color(0xFFFFFFFF), // White cards
    cardSecondaryBackground = Color(0xFFF8F8F8), // Very light gray
    accent = Color(0xFFef5e07), // Orange
    successColor = Color(0xFF34C759), // Green
    errorColor = Color(0xFFFF3B30), // Red
    warningColor = Color(0xFFFF9500), // Orange
    statusOnline = Color(0xFF34C759), // Green
    statusOffline = Color(0xFF8E8E93), // Gray
    divider = Color(0xFFE5E5EA), // Light divider
)

internal val DarkColorScheme = AppColorScheme(
    primaryBackground = Color(0xFF000000), // Black
    primaryText = Color(0xFFFFFFFF), // White
    primaryButton = Color(0xFF0A84FF), // Lighter blue
    secondaryBackground = Color(0xFF1C1C1E), // Dark gray
    secondaryText = Color(0xFF98989D), // Light gray
    secondaryButton = Color(0xFF30D158), // Lighter green
    cardBackground = Color(0xFF1C1C1E), // Dark cards
    cardSecondaryBackground = Color(0xFF2C2C2E), // Slightly lighter dark
    accent = Color(0xFFFF6B35), // Lighter orange for dark mode
    successColor = Color(0xFF30D158), // Lighter green
    errorColor = Color(0xFFFF453A), // Lighter red
    warningColor = Color(0xFFFF9F0A), // Lighter orange
    statusOnline = Color(0xFF30D158), // Lighter green
    statusOffline = Color(0xFF98989D), // Light gray
    divider = Color(0xFF38383A), // Dark divider
)

// Additional colors for consistency
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Orange = Color(0xFFef5e07)