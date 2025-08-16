package com.breastcancer.breastcancercare.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TransformOrigin
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.database.local.types.EventType
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPadding
import com.breastcancer.breastcancercare.theme.DefaultVerticalPadding
import com.breastcancer.breastcancercare.viewmodel.CalendarViewModel
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
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun CalendarScreen(calendarViewModel: CalendarViewModel = koinViewModel()) {
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

    val selectedTab by calendarViewModel.selectedTab.collectAsStateWithLifecycle()
    val selectedDate by calendarViewModel.selectedDate.collectAsStateWithLifecycle()
    val onDateClicked: (selectedDate: LocalDate) -> Unit = calendarViewModel::changeSelectedDate

    Box(modifier = Modifier.fillMaxSize()) {
        VerticalCalendar(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            state = state,
            dayContent = {
                if (it.position == DayPosition.MonthDate)
                    Day(
                        it,
                        selectedDate = selectedDate,
                        onDateClicked = onDateClicked
                    )
            },
            monthContainer = { month, container ->
                Column(modifier = Modifier.padding(vertical = DefaultVerticalPadding)) {
                    MonthHeader(month)
                    container.invoke()
                }
            },
            contentPadding = PaddingValues(
                horizontal = DefaultHorizontalPadding,
                vertical = DefaultVerticalPadding
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(0.85f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0f)
                        )
                    )
                )
                .padding(
                    horizontal = DefaultHorizontalPadding
                ).padding(bottom = DefaultVerticalPadding * 2).align(Alignment.TopCenter)
        ) {
            for (dayOfWeek in listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    text = dayOfWeek,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        BottomInfoCard(
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            selectedTab = selectedTab,
            onTabSelected = {
                calendarViewModel.changeTab(it)
            }
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun Day(
    day: CalendarDay,
    selectedDate: LocalDate,
    onDateClicked: (selectedDate: LocalDate) -> Unit
) {
    val currentDate by remember { mutableStateOf(LocalDate.now()) }
    val dayText: @Composable (selected: Boolean) -> Unit = { selected ->
        Text(
            text = day.date.day.toString(),
            color =
                if (selected) MaterialTheme.colorScheme.onPrimary
                else if (day.date == currentDate) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onBackground,
            style =
                LocalTextStyle.current.copy(fontWeight = if (day.date == currentDate || selected) FontWeight.Bold else FontWeight.Normal)
        )
    }
    Box(
        modifier = Modifier
            .aspectRatio(1f).clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                if (day.position == DayPosition.MonthDate)
                    onDateClicked(day.date)
            },
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = selectedDate == day.date,
            label = "SelectedDate",
            transitionSpec = {
                (scaleIn(
                    initialScale = 0.82f,
                    animationSpec = tween(220, delayMillis = 90),
                    transformOrigin = TransformOrigin.Center
                ))
                    .togetherWith(fadeOut(animationSpec = tween(90)))
            }) { selected ->
            when (selected) {
                true -> Box(
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ).padding(5.dp)
                ) {
                    dayText(selected)
                }

                else -> dayText(selected)
            }
        }
    }
}

@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
            text = calendarMonth.yearMonth.month.name,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}

@Composable
fun BottomInfoCard(
    modifier: Modifier = Modifier,
    selectedTab: Int,
    openHeightFraction: Float = 0.7f,     // sheet height
    closedVisibleFraction: Float = 0.3f,  // visible part when closed
    onTabSelected: (index: Int) -> Unit
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
                        .clickable(
                            interactionSource = interaction,
                            indication = ripple(bounded = false)
                        ) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.VirtualKey)
                            animateToOffset(if (isOpen) closedOffsetPx else 0f)
                        }.semantics { role = Role.Button }
                )
                EventSection(
                    modifier = Modifier.fillMaxWidth(),
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )
            }
        }
    }
}

@Composable
private fun EventSection(
    modifier: Modifier = Modifier,
    selectedTab: Int,
    onTabSelected: (index: Int) -> Unit
) {
    Column {
        TabRow(modifier = modifier, selectedTabIndex = selectedTab, tabs = {
            Tab(selected = EventType.Event.ordinal == selectedTab, onClick = {
                onTabSelected(
                    EventType.Event.ordinal
                )
            }) {
                Text(modifier = Modifier.padding(10.dp), text = "Events")
            }
            Tab(selected = EventType.Program.ordinal == selectedTab, onClick = {
                onTabSelected(
                    EventType.Program.ordinal
                )
            }) {
                Text(modifier = Modifier.padding(10.dp), text = " Programs")
            }
        })
    }
}
