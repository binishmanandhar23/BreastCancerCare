package com.breastcancer.breastcancercare.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.lerp
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.database.local.types.FrequencyType
import com.breastcancer.breastcancercare.default_bg_image
import com.breastcancer.breastcancercare.screens.Screens
import com.breastcancer.breastcancercare.screens.SubScreens
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.kizitonwose.calendar.core.plusDays
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
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
        FrequencyType.Daily -> {
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

        FrequencyType.Yearly -> {
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
            }
            else {
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
                    placeable.placeRelative(leftPx, 50)
                }
            }
        }
    )
}


/** 0f..1f selection “progress” of a tab based on pager’s position/offset. */
@Composable
fun tabSelectionProgress(index: Int, pagerState: androidx.compose.foundation.pager.PagerState): Float {
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
    val r  = min(size.width, size.height) / 2f
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