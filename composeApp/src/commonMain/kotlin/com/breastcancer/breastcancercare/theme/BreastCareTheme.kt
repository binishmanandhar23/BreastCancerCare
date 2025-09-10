package com.breastcancer.breastcancercare.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

val LightAppColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    background = BackgroundColor,
    onBackground = TextColorLight,
    surface = ColorWhite,
    // ... define other Material3 colors
)

val DarkAppColorScheme = darkColorScheme(
    primary = PrimaryColor, // Example dark primary
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    background = PrimaryColor,
    onBackground = TextColorDark,
    surface = PrimaryColor,
    // ... define other Material3 colors
)
