package com.breastcancer.breastcancercare.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall

@Composable
fun LazyColumnWithStickyFooter(
    modifier: Modifier,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    stickyFooter: @Composable (BoxScope.() -> Unit)? = null,
    bottomSpacer: Dp = 100.dp,
    forceSpacer: Boolean = false,
    content: LazyListScope.() -> Unit
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.matchParentSize(),
            reverseLayout = reverseLayout,
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
            content = {
                content(this)
                if (stickyFooter != null || forceSpacer)
                    item {
                        Spacer(modifier = Modifier.size(bottomSpacer))
                    }
            })
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            stickyFooter?.invoke(this)
        }
    }
}


@Composable
fun LazyColumnCollapsibleHeader(
    modifier: Modifier = Modifier,
    headerHeight: Dp = 80.dp,          // full height of the first item
    collapseWithFade: Boolean = false,  // true = shrink height as it fades
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =  if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    header: @Composable BoxScope.() -> Unit,
    content: LazyListScope.() -> Unit
) {
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    // How far (in px) we use to fully fade the header.
    val fadeRangePx = with(density) { (headerHeight * 0.6f).roundToPx().toFloat() }

    // Progress of "hiding": 0f at top (fully visible), 1f when header is fully hidden
    val hideProgress by remember {
        derivedStateOf {
            when {
                listState.firstVisibleItemIndex > 0 -> 1f
                else -> (listState.firstVisibleItemScrollOffset / fadeRangePx)
                    .coerceIn(0f, 1f)
            }
        }
    }

    // Animate alpha and (optionally) height for smoothness when index flips 0<->1.
    val animatedAlpha by animateFloatAsState(targetValue = 1f - hideProgress, label = "headerAlpha")

    val animatedHeight by animateDpAsState(
        targetValue = if (collapseWithFade) lerp(headerHeight, 0.dp, hideProgress) else headerHeight,
        label = "headerHeight"
    )

    // A slight upward shift adds to the “sliding away” feel (in px).
    val shiftUpPx = with(density) { 4.dp.toPx() }
    val translationYPx = -shiftUpPx * hideProgress

    LazyColumn(
        state = listState,
        modifier = modifier,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        // First item: the fading (and optionally collapsing) header
        item(key = "header") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animatedHeight)
                    .graphicsLayer {
                        alpha = animatedAlpha
                        translationY = translationYPx
                    }
                    .padding(horizontal = DefaultHorizontalPaddingMedium, vertical = DefaultVerticalPaddingSmall)
            ) {
                header()
            }
        }
        content()
    }
}