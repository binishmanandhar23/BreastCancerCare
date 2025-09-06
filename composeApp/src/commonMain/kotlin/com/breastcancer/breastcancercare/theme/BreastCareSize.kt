package com.breastcancer.breastcancercare.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val RoundedCornerSize = 30.dp
val DefaultElevation = 5.dp
val DefaultHorizontalPaddingSmall = 10.dp
val DefaultHorizontalPaddingMedium = (10 * 1.5).dp
val DefaultHorizontalPaddingLarge = (10 * 2).dp

val DefaultVerticalPaddingLarge = (15 * 2).dp
val DefaultVerticalPaddingMedium = 15.dp

val DefaultVerticalPaddingSmall = (15 / 2).dp

val DefaultSpacerSize = 50.dp

val DefaultTopHeaderTextSize = 50.sp

@Composable
fun spToDp(sp: TextUnit): Dp = with(LocalDensity.current) {
    sp.toPx().toDp()   // sp -> px (uses fontScale) -> dp (uses density)
}