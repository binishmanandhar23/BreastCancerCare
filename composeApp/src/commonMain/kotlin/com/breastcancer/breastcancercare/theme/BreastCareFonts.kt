package com.breastcancer.breastcancercare.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun BreastCareTypography(extraTextSize: Int = 0) = Typography().run {
    val headlineFontFamily = HeadlineFontFamily()
    val bodyFontFamily = BodyFontFamily()

    copy(
        displayLarge = displayLarge.copy(fontFamily = headlineFontFamily, fontSize = (displayLarge.fontSize.value + extraTextSize).sp),
        displayMedium = displayMedium.copy(fontFamily = headlineFontFamily, fontSize = (displayMedium.fontSize.value + extraTextSize).sp),
        displaySmall = displaySmall.copy(fontFamily = headlineFontFamily, fontSize = (displaySmall.fontSize.value + extraTextSize).sp),
        headlineLarge = headlineLarge.copy(fontFamily = headlineFontFamily, fontSize = (headlineLarge.fontSize.value + extraTextSize).sp),
        headlineMedium = headlineMedium.copy(fontFamily = headlineFontFamily, fontSize = (headlineMedium.fontSize.value + extraTextSize).sp),
        headlineSmall = headlineSmall.copy(fontFamily = headlineFontFamily, fontSize = (headlineSmall.fontSize.value + extraTextSize).sp),
        titleLarge = titleLarge.copy(fontFamily = bodyFontFamily, fontSize = (titleLarge.fontSize.value + extraTextSize).sp),
        titleMedium = titleMedium.copy(fontFamily = bodyFontFamily, fontSize = (titleMedium.fontSize.value + extraTextSize).sp),
        titleSmall = titleSmall.copy(fontFamily = bodyFontFamily, fontSize = (titleSmall.fontSize.value + extraTextSize).sp),
        bodyLarge = bodyLarge.copy(fontFamily = bodyFontFamily, fontSize = (bodyLarge.fontSize.value + extraTextSize).sp),
        bodyMedium = bodyMedium.copy(fontFamily = bodyFontFamily, fontSize = (bodyMedium.fontSize.value + extraTextSize).sp),
        bodySmall = bodySmall.copy(fontFamily = bodyFontFamily, fontSize = (bodySmall.fontSize.value + extraTextSize).sp),
        labelLarge = labelLarge.copy(fontFamily = bodyFontFamily, fontSize = (labelLarge.fontSize.value + extraTextSize).sp),
        labelMedium = labelMedium.copy(fontFamily = bodyFontFamily, fontSize = (labelMedium.fontSize.value + extraTextSize).sp),
        labelSmall = labelSmall.copy(fontFamily = bodyFontFamily, fontSize = (labelSmall.fontSize.value + extraTextSize).sp)
    )
}