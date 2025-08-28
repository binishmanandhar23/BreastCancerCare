package com.breastcancer.breastcancercare.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
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