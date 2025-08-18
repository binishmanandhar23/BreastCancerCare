package com.breastcancer.breastcancercare.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

val LightAppColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    background = BackgroundColor,
    onBackground = TextColorLight,
    // ... define other Material3 colors
)

val DarkAppColorScheme = darkColorScheme(
    primary = PrimaryColor, // Example dark primary
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    background = PrimaryColor,
    onBackground = TextColorDark
    // ... define other Material3 colors
)
