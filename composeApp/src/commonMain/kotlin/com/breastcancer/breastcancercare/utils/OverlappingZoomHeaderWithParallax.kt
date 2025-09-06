package com.breastcancer.breastcancercare.utils

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import kotlin.math.max
import kotlin.math.min

@Composable
fun OverlappingZoomHeaderWithParallax(
    modifier: Modifier = Modifier,
    header: @Composable (modifier: Modifier) -> Unit,
    baseHeaderHeight: Dp = 250.dp,
    defaultOverlap: Dp = 24.dp,          // <-- how much to overlap at rest
    maxExtraPullPx: Float = 600f,
    parallaxMultiplier: Float = 0.5f,
    backButton: Boolean = true,
    onBackClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    var pullTarget by remember { mutableStateOf(0f) }
    val pull by animateFloatAsState(
        targetValue = pullTarget,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "overscrollPull"
    )

    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput) {
                    val dy = available.y
                    if (dy < 0f && pullTarget > 0f) {
                        val newVal = max(0f, pullTarget + dy)
                        val consumed = newVal - pullTarget
                        pullTarget = newVal
                        return Offset(0f, consumed)
                    }
                    if (dy > 0f && scrollState.value == 0) {
                        val newVal = min(maxExtraPullPx, pullTarget + dy)
                        val consumed = newVal - pullTarget
                        pullTarget = newVal
                        return Offset(0f, consumed)
                    }
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (source == NestedScrollSource.UserInput && scrollState.value == 0 && available.y != 0f) {
                    val newVal = (pullTarget + available.y).coerceIn(0f, maxExtraPullPx)
                    val consumed = newVal - pullTarget
                    pullTarget = newVal
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity) =
                Velocity.Zero.also { pullTarget = 0f }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity) =
                Velocity.Zero.also { pullTarget = 0f }
        }
    }

    val density = LocalDensity.current
    val headerHeight = baseHeaderHeight + with(density) { pull.toDp() }
    val headerHeightPx = with(density) { headerHeight.roundToPx() }
    val baseOverlapPx = with(density) { defaultOverlap.roundToPx() }

    // Start already overlapped by `defaultOverlap`, then keep collapsing up to the header height
    val overlapOffsetY = -min(scrollState.value + baseOverlapPx, baseOverlapPx)

    // *** Key fix: pad the bottom by the current overlap so no gap can appear ***
    val dynamicBottomPadding = with(density) { (-overlapOffsetY).toDp() }

    // Parallax: move the image slower than content
    val parallaxOffsetY = (-scrollState.value * parallaxMultiplier).toInt().coerceIn(-headerHeightPx, 0)

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .nestedScroll(connection)
        ) {
            // BACK: Stretchy header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight),
                contentAlignment = Alignment.TopCenter
            ) {
                header(Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, parallaxOffsetY) })
            }

            // FRONT: Scrollable content, drawn above and overlapping by default
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val minHeight = maxHeight + headerHeight
                Column(
                    modifier = Modifier.heightIn(min = minHeight)
                        .zIndex(1f)
                        .offset { IntOffset(0, overlapOffsetY) }  // includes the default overlap
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(top = headerHeight),             // layout below header; offset pulls it over
                    content = content
                )
            }
        }
        if (backButton)
            Box(
                modifier = Modifier.padding(
                    horizontal = DefaultHorizontalPaddingMedium,
                    vertical = DefaultVerticalPaddingMedium
                ).background(
                    color = MaterialTheme.colorScheme.background.copy(
                        alpha = 0.5f
                    ), shape = CircleShape
                ).clip(CircleShape).clickable{
                    onBackClick()
                }.padding(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back button"
                )
            }

    }
}
