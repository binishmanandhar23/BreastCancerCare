package com.breastcancer.breastcancercare.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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