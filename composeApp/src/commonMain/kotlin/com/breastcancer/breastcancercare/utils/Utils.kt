package com.breastcancer.breastcancercare.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabPosition
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.lerp
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.database.local.types.FrequencyType
import com.breastcancer.breastcancercare.default_bg_image
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.kizitonwose.calendar.core.plusDays
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sign
import kotlin.math.sin
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@Composable
fun rememberWindowSizeDp(): Pair<Dp, Dp> {
    val density = LocalDensity.current
    val sizePx = LocalWindowInfo.current.containerSize   // IntSize (px)
    val wDp = with(density) { sizePx.width.toDp() }
    val hDp = with(density) { sizePx.height.toDp() }
    return wDp to hDp
}


@OptIn(ExperimentalTime::class)
fun getHomeGreetingText(userName: String? = null): String {
    val instant = Clock.System.now()
    val zone = TimeZone.currentSystemDefault()
    val local: LocalDateTime = instant.toLocalDateTime(zone)

    return when (local.hour) {
        in 5..11 -> "Good Morning!"
        in 12..17 -> "Good Afternoon!"
        else -> "Good Evening!"
    }.let {
        if (userName != null)
            "$it\n$userName"
        else
            it
    }
}

@Composable
fun DefaultImage(
    modifier: Modifier = Modifier,
    resource: DrawableResource = Res.drawable.default_bg_image,
    contentScale: ContentScale = ContentScale.Fit
) = Image(
    painterResource(resource = resource),
    contentDescription = "Default Image",
    modifier = modifier,
    contentScale = contentScale
)

@Composable
fun DefaultSpacer(size: Dp = DefaultHorizontalPaddingSmall) = Spacer(modifier = Modifier.size(size))

fun checkIfDateHasProgram(
    frequencyType: FrequencyType,
    selectedDate: LocalDate,
    startDate: LocalDate,
    endDate: LocalDate
): Boolean {
    when (frequencyType) {
        FrequencyType.Ongoing -> {
            var date = startDate
            var i = 0
            while (date <= endDate) {
                date = startDate.plusDays(i)
                if (date == selectedDate)
                    return true
                else
                    i++
            }
        }

        FrequencyType.Weekly -> {
            var date = startDate
            var i = 0
            while (date <= endDate) {
                date = startDate.plus(i, DateTimeUnit.WEEK)
                if (date == selectedDate)
                    return true
                else
                    i++
            }
        }

        FrequencyType.Monthly -> {
            var date = startDate
            var i = 0
            while (date <= endDate) {
                date = startDate.plus(i, DateTimeUnit.MONTH)
                if (date == selectedDate)
                    return true
                else
                    i++
            }
        }

        FrequencyType.Series -> {
            var date = startDate
            var i = 0
            while (date <= endDate) {
                date = startDate.plus(i, DateTimeUnit.YEAR)
                if (date == selectedDate)
                    return true
                else
                    i++
            }
        }

        FrequencyType.OnceOff -> return selectedDate == startDate
        FrequencyType.Block -> return false
    }
    return false
}

/** Animate the indicator between [currentPage] and the next page using the drag fraction. */

fun Modifier.pagerTabIndicatorOffset(
    pagerState: androidx.compose.foundation.pager.PagerState,
    tabPositions: List<TabPosition>
): Modifier = composed {
    this.then(
        Modifier.layout { measurable, constraints ->
            if (tabPositions.isEmpty()) {
                // nothing to place yet
                val placeable = measurable.measure(constraints)
                layout(constraints.maxWidth, placeable.height) { placeable.placeRelative(0, 0) }
            } else {
                val current = pagerState.currentPage.coerceIn(0, tabPositions.lastIndex)
                val offset = pagerState.currentPageOffsetFraction

                // choose the adjacent target tab based on swipe direction
                val target = (current + offset.sign.toInt())
                    .coerceIn(0, tabPositions.lastIndex)

                val currentTab = tabPositions[current]
                val targetTab = tabPositions[target]

                val fraction = abs(offset).coerceIn(0f, 1f)

                // Interpolate left/right in Dp for stability, then convert to px
                fun lerpDp(a: Dp, b: Dp, f: Float) = lerp(a, b, f)

                val leftDp = lerpDp(currentTab.left, targetTab.left, fraction)
                val rightDp = lerpDp(currentTab.right, targetTab.right, fraction)

                val leftPx = leftDp.roundToPx()
                val widthPx = (rightDp - leftDp).roundToPx().coerceAtLeast(0)

                // 🔑 Measure child at the exact width we need
                val placeable = measurable.measure(
                    constraints.copy(minWidth = widthPx, maxWidth = widthPx)
                )

                layout(constraints.maxWidth, placeable.height) {
                    placeable.placeRelative(leftPx, 55)
                }
            }
        }
    )
}


/** 0f..1f selection “progress” of a tab based on pager’s position/offset. */
@Composable
fun tabSelectionProgress(
    index: Int,
    pagerState: androidx.compose.foundation.pager.PagerState
): Float {
    // how far is this tab from the pager’s animated position?
    val raw = (index - pagerState.currentPage - pagerState.currentPageOffsetFraction).absoluteValue
    val clamped = 1f - raw.coerceIn(0f, 1f) // 1 when focused, 0 when far
    return clamped
}

val TriangleShape = GenericShape { size, _ ->
    moveTo(size.width / 2f, 0f)           // top
    lineTo(0f, size.height)               // bottom-left
    lineTo(size.width, size.height)       // bottom-right
    close()
}

private fun regularPolygonShape(sides: Int, rotationDeg: Float = -90f) = GenericShape { size, _ ->
    require(sides >= 3) { "Polygon must have at least 3 sides" }
    val cx = size.width / 2f
    val cy = size.height / 2f
    val r = min(size.width, size.height) / 2f
    val start = rotationDeg * (PI / 180f)

    repeat(sides) { i ->
        val a = start + i * (2 * PI / sides)
        val x = cx + r * cos(a).toFloat()
        val y = cy + r * sin(a).toFloat()
        if (i == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

// A point-up regular pentagon
val PentagonShape = regularPolygonShape(5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Modifier.roundedClickableNoClip(
    onClick: () -> Unit,
    shape: Shape = MaterialTheme.shapes.medium,
    containerColor: Color = Color.Unspecified,   // pass if you want a shaped background
    pressedOverlay: Color = Color.Black.copy(alpha = 0.08f),
    enableRipple: Boolean = true
): Modifier = composed(inspectorInfo = debugInspectorInfo {
    name = "roundedClickableNoClip"
    properties["shape"] = shape
}) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    // Build ripple indication if enabled
    val ripple: Indication? = if (enableRipple) ripple(bounded = true) else null

    // We return a small layering DSL: the parent Box is clickable (no ripple),
    // content is placed normally (NOT clipped), and we draw an overlay Box
    // that is clipped to shape and carries the ripple + pressed tint.
    this.then(
        Modifier
            // 1) Make the whole thing clickable, but don't attach indication here.
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            // 2) Draw shaped background behind content (does not clip the child).
            .drawBehind {
                if (containerColor.isSpecified) {
                    val outline = shape.createOutline(size, layoutDirection, this)
                    drawOutline(outline = outline, color = containerColor)
                }
            }
            // 3) Add an overlay layer that matches the parent's size, clips to shape,
            //    and hosts the ripple + pressed tint. This overlay does not affect
            //    the children's clipping.
            .then(
                Modifier.layout { measurable, constraints ->
                    // We need to inject a child overlay; layout returns the same placeable.
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                }.then(
                    Modifier
                        .wrapContentSize()
                        .clip(shape) // only the OVERLAY is clipped
                        .indication(interaction, ripple)
                        .drawWithContent {
                            // Draw the pressed overlay *above* content (no content here, it's an overlay box)
                            if (pressed) {
                                val outline = shape.createOutline(size, layoutDirection, this)
                                drawOutline(outline = outline, color = pressedOverlay)
                            }
                        }
                )
            )
    )
}

@OptIn(ExperimentalTime::class)
fun emojiFor(): String {
    val zone = TimeZone.currentSystemDefault()
    val now: LocalTime = Clock.System.now().toLocalDateTime(zone).time
    return when (now.hour) {
        in 5..8 -> "🌅"  // sunrise
        in 9..16 -> "🌞"  // daytime / afternoon sun
        in 17..19 -> "🌇"  // sunset
        else -> "🌙"  // night
    }
}
