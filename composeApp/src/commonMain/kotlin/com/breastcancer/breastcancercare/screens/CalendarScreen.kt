package com.breastcancer.breastcancercare.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPadding
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun CalendarScreen() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(50) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )
    Box(modifier = Modifier.fillMaxSize()) {
        VerticalCalendar(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            state = state,
            dayContent = { Day(it) },
            monthContainer = { month, container ->
                Column(modifier = Modifier.padding(vertical = DefaultVerticalPadding)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            MonthHeader(month)
                            container.invoke()
                        }
                    }
                }
            },
            contentPadding = PaddingValues(
                horizontal = DefaultHorizontalPadding,
                vertical = DefaultVerticalPadding
            )
        )
        BottomInfoCard(modifier = Modifier.align(alignment = Alignment.BottomCenter))
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun Day(day: CalendarDay) {
    Box(
        modifier = Modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val currentDate by remember { mutableStateOf(LocalDate.now()) }
        Text(
            text = day.date.day.toString(),
            color = if (day.date == currentDate) MaterialTheme.colorScheme.primary else if (day.position == DayPosition.MonthDate) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondary.copy(
                alpha = 0.5f
            ),
            style = LocalTextStyle.current.copy(fontWeight = if (day.date == currentDate) FontWeight.Bold else FontWeight.Normal)
        )
    }
}

@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
    val daysOfWeek = calendarMonth.weekDays.first().map { it.date.dayOfWeek }
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
            text = calendarMonth.yearMonth.month.name,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    text = dayOfWeek.name.substring(0, 3),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun BottomInfoCard(
    modifier: Modifier = Modifier,
    openHeightFraction: Float = 0.7f,     // sheet height
    closedVisibleFraction: Float = 0.3f  // visible part when closed
) {
    val hapticFeedback = LocalHapticFeedback.current
    // Parent size needed to compute pixel offsets
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scope = rememberCoroutineScope()
        val parentHeightPx = with(LocalDensity.current) { maxHeight.toPx() }

        // Height of the sheet in px (fixed at 50% of screen by default)
        val sheetHeightPx = parentHeightPx * openHeightFraction

        // Closed offset pushes the sheet down so that only `closedVisibleFraction` is visible
        val closedOffsetPx = (sheetHeightPx - parentHeightPx * closedVisibleFraction)
            .coerceAtLeast(0f)

        // 0f = fully open, closedOffsetPx = fully closed
        val offsetAnim = remember { Animatable(closedOffsetPx) }

        // Gesture handling
        val dragState = rememberDraggableState { delta ->
            val new = (offsetAnim.value + delta).coerceIn(0f, closedOffsetPx)
            scope.launch {
                offsetAnim.snapTo(new)
            }
        }

        val isOpen by remember {
            derivedStateOf { offsetAnim.value < closedOffsetPx / 2f }
        }

        val animateToOffset = { target: Float ->
            scope.launch {
                offsetAnim.animateTo(
                    target,
                    spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }

        // The card itself
        Card(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(openHeightFraction)
                .offset { IntOffset(0, offsetAnim.value.roundToInt()) }
                .draggable(
                    state = dragState,
                    orientation = Orientation.Vertical,
                    onDragStopped = { velocity ->
                        // Snap to the nearest anchor (open/closed)
                        val midpoint = closedOffsetPx / 2f
                        val target = when {
                            velocity > 1500f -> closedOffsetPx // fling down
                            velocity < -1500f -> 0f            // fling up
                            offsetAnim.value > midpoint -> closedOffsetPx
                            else -> 0f
                        }
                        animateToOffset(target)
                    }
                ),
            shape = MaterialTheme.shapes.large.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 50.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = DefaultHorizontalPadding,
                        vertical = DefaultVerticalPadding
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val interaction = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .height(10.dp)
                        .width(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(interactionSource = interaction, indication = ripple(bounded = false)) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.VirtualKey)
                            animateToOffset(if(isOpen) closedOffsetPx else 0f)
                        }.semantics{ role = Role.Button }
                )
                Column {
                    Text(
                        "Activities",
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
