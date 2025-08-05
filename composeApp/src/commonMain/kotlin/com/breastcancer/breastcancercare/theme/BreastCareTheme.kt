package com.breastcancer.breastcancercare.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

val LightAppColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = BackgroundColor,
    onBackground = PrimaryTextColor,
    // ... define other Material3 colors
)

val DarkAppColorScheme = darkColorScheme(
    primary = PrimaryColor, // Example dark primary
    secondary = SecondaryColor,
    background = PrimaryColor,
    onBackground = BackgroundColor
    // ... define other Material3 colors
)
