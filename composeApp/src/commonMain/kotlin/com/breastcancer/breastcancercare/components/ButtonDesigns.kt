package com.breastcancer.breastcancercare.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBarIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    text: String,
    tint: Color = MaterialTheme.colorScheme.onBackground,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.clip(shape = MaterialTheme.shapes.medium).clickable(onClick = onClick ?: {}),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = text,
            tint = tint
        )
        Text(text = text, color = tint)
    }
}

@Composable
fun CenterButton(
    modifier: Modifier = Modifier,
    onSizeChange: ((IntSize) -> Unit)? = null,
    onClick: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary
                    )
                ),
                shape = CircleShape
            )
            .clickable(MutableInteractionSource(), null) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick.invoke()
            }.onSizeChanged(onSizeChange ?: {})
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add icon",
            modifier = Modifier
                .padding(19.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}