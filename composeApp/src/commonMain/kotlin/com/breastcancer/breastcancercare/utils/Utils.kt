package com.breastcancer.breastcancercare.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.screens.Screens
import com.breastcancer.breastcancercare.screens.SubScreens

fun getNavigationRoute(mainScreen: Screens, subScreen: SubScreens? = null): String =
    "${mainScreen.screen}${if (subScreen != null) "/" + subScreen.screen else ""}"

@Composable
fun rememberWindowSizeDp(): Pair<Dp, Dp> {
    val density = LocalDensity.current
    val sizePx = LocalWindowInfo.current.containerSize   // IntSize (px)
    val wDp = with(density) { sizePx.width.toDp() }
    val hDp = with(density) { sizePx.height.toDp() }
    return wDp to hDp
}