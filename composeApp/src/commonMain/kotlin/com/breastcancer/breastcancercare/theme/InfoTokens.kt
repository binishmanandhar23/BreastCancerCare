package com.breastcancer.breastcancercare.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

object InfoDimens {
    val ScreenHPadding = DefaultHorizontalPaddingSmall
    val ScreenVPadding = DefaultVerticalPaddingSmall
    val CardSpacing = DefaultVerticalPaddingSmall
    val SectionSpacing = 24.dp
    val CardCorner = RoundedCornerSize
}

object InfoAnim {
    const val Expand = 200
    const val Fade = 120
}

object InfoColors {
    @Composable
    fun faqCard(index: Int): Color =
        if (index % 2 == 0) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondary

    @Composable
    fun onFaqCard(): Color = MaterialTheme.colorScheme.onPrimary

    @Composable
    fun meta(): Color = MaterialTheme.colorScheme.onSurfaceVariant
}
