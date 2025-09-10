package com.breastcancer.breastcancercare.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.epilogue_medium
import com.breastcancer.breastcancercare.epilogue_regular
import com.breastcancer.breastcancercare.familjengrotesk_medium
import com.breastcancer.breastcancercare.familjengrotesk_regular

@Suppress("ComposableNaming")
@Composable
fun BodyFontFamily() = FontFamily(
    Font( Res.font.epilogue_regular, weight = FontWeight.Normal),
    Font(Res.font.epilogue_medium,  weight = FontWeight.Medium)
)

@Suppress("ComposableNaming")
@Composable
fun HeadlineFontFamily() = FontFamily(
    Font( Res.font.familjengrotesk_regular, weight = FontWeight.Normal),
    Font(Res.font.familjengrotesk_medium,  weight = FontWeight.Medium)
)

@Suppress("ComposableNaming")
@Composable
fun BreastCareTypography() = Typography().run {
    val headlineFontFamily = HeadlineFontFamily()
    val bodyFontFamily = BodyFontFamily()

    copy(
        displayLarge = displayLarge.copy(fontFamily = headlineFontFamily),
        displayMedium = displayMedium.copy(fontFamily = headlineFontFamily),
        displaySmall = displaySmall.copy(fontFamily = headlineFontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = headlineFontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = headlineFontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = headlineFontFamily),
        titleLarge = titleLarge.copy(fontFamily = bodyFontFamily),
        titleMedium = titleMedium.copy(fontFamily = bodyFontFamily),
        titleSmall = titleSmall.copy(fontFamily = bodyFontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = labelSmall.copy(fontFamily = bodyFontFamily)
    )
}