package com.breastcancer.breastcancercare.components.slider// commonMain

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/** State to control/observe the slider externally (e.g., reset programmatically). */
class SlideToProceedState internal constructor(
    initialCompleted: Boolean
) {
    internal val offsetPx = Animatable(0f) // current thumb left offset in px (0..dragRange)
    internal var dragRangePx by mutableStateOf(1f)
    var isCompleted by mutableStateOf(initialCompleted)
        internal set

    val progress: Float
        get() = (offsetPx.value / dragRangePx).coerceIn(0f, 1f)

    suspend fun complete() {
        offsetPx.animateTo(dragRangePx, tween(220))
        isCompleted = true
    }

    suspend fun reset() {
        offsetPx.animateTo(0f, tween(220))
        isCompleted = false
    }
}

@Composable
fun rememberSlideToProceedState(initialCompleted: Boolean = false) =
    remember { SlideToProceedState(initialCompleted) }

/**
 * A "Slide to proceed" control.
 *
 * - Drag the thumb to the end to trigger [onComplete].
 * - If released before [completeThreshold], it animates back.
 * - Set [resetOnComplete] to true if you want it to slide back after callback.
 */
@Composable
fun SlideToProceed(
    modifier: Modifier = Modifier,
    state: SlideToProceedState = rememberSlideToProceedState(),
    text: String = "Slide to proceed",
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.onPrimary,
    height: Dp = 56.dp,
    thumbPadding: Dp = 4.dp,
    thumbWidth: Dp = height,                     // for a round thumb, keep == height
    shape: Shape = MaterialTheme.shapes.medium,
    thumbShape: Shape = shape,            // or pass shape for matching corners
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
    thumbColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    enabled: Boolean = true,
    completeThreshold: Float = 0.82f, // proportion of width to trigger completion
    resetOnComplete: Boolean = false,
    onClick: (() -> Unit)? = null,
    onComplete: () -> Unit
) {
    val density = LocalDensity.current
    val layoutDir = LocalLayoutDirection.current
    val coroutineScope = rememberCoroutineScope()

    // We measure in a BoxWithConstraints so we know our drag range.
    BoxWithConstraints(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .clip(shape)
            .background(trackColor)
            .semantics(mergeDescendants = true) {
                stateDescription = if (state.isCompleted) "Completed" else "Not completed"
                if (enabled && !state.isCompleted) {
                    // Expose as a progress control to accessibility
                    setProgress { target ->
                        // target is 0..1; snap and maybe complete
                        true
                    }
                }
            }.then(if(onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        val hPadPx = with(density) { thumbPadding.toPx() }
        val thumbWpx = with(density) { thumbWidth.toPx() }
        val dragRangePx = (constraints.maxWidth - 2 * hPadPx - thumbWpx).coerceAtLeast(1f)
        // expose to state so progress computes correctly
        LaunchedEffect(dragRangePx) { state.dragRangePx = dragRangePx }

        // Ensure offset respects completion state on first composition
        LaunchedEffect(state.isCompleted, dragRangePx) {
            if (state.isCompleted)
                state.offsetPx.snapTo(dragRangePx)
            else
                state.offsetPx.snapTo(0f)
        }

        // Progress overlay
        val progress = state.progress
        Box(
            Modifier
                .matchParentSize()
                .clip(shape)
        ) {
            // Fill from start to thumb leading edge
            val progWidth = hPadPx + thumbWpx * 0.5f + dragRangePx * progress
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(with(density) { progWidth.toDp() })
                    .background(progressColor)
            )
        }

        // Center text that fades as we progress
        val textAlpha = 1f - progress * 1.2f
        Box(Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
            Text(
                text = if (state.isCompleted) "Proceeding" else text,
                color = textColor.copy(alpha = textAlpha.coerceIn(0f, 1f)),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Thumb (draggable)
        val draggableState = rememberDraggableState { delta ->
            if (!enabled || state.isCompleted) return@rememberDraggableState
            // Respect RTL: reverse drag direction
            val signed = if (layoutDir == LayoutDirection.Rtl) -delta else delta
            val newPx = (state.offsetPx.value + signed).coerceIn(0f, dragRangePx)
            // snap for immediate response
            coroutineScope.launch {
                state.offsetPx.snapTo(newPx)
            }
        }

        val thumbsX = when (layoutDir) {
            LayoutDirection.Ltr -> hPadPx + state.offsetPx.value
            LayoutDirection.Rtl -> hPadPx + (dragRangePx - state.offsetPx.value)
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(thumbsX.roundToInt(), 0) }
                .width(thumbWidth)
                .fillMaxHeight()
                .clip(thumbShape)
                .background(thumbColor)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = draggableState,
                    enabled = enabled && !state.isCompleted,
                    onDragStopped = {
                        if (!enabled || state.isCompleted) return@draggable
                        val reached = state.progress >= completeThreshold
                        // animate to end or back
                        coroutineScope.launch {
                            if (reached) {
                                // complete
                                state.complete()
                                onComplete()
                                if (resetOnComplete) {
                                    delay(300)
                                    state.reset()
                                }
                            } else {
                                state.reset()
                            }
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            // You can put an icon/arrow here:
            if (icon != null)
                Icon(imageVector = icon, contentDescription = text, tint = iconTint)
            else
                Text("➤", color = Color.White)
        }
    }
}
