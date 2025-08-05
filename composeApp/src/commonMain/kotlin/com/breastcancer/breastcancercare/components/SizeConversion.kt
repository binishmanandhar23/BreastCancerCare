package com.breastcancer.breastcancercare.components

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

fun Density.convertIntSizeToDpSize(intSizeInPx: IntSize, getDpSize: (DpSize) -> Unit) {
    // Get the current Density from LocalDensity
    // Convert the Int pixel value to Dp using the density scope
    getDpSize(with(this) {
        DpSize(width = intSizeInPx.width.toDp(), intSizeInPx.height.toDp())
    })
}