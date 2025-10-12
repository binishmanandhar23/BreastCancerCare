package com.breastcancer.breastcancercare.models

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource

data class TutorialDTO(
    val page: Int,
    val title: String,
    val description: String,
    val borderWidth: Dp = 5.dp,
    val phoneImage: DrawableResource,
    val tabletImage: DrawableResource? = null
)
