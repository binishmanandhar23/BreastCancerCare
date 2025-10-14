package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.ActivityDesign
import com.breastcancer.breastcancercare.components.LazyColumnWithStickyFooter
import com.breastcancer.breastcancercare.components.icons.Counselling
import com.breastcancer.breastcancercare.components.icons.Nurse
import com.breastcancer.breastcancercare.database.local.types.GeneralActivityType
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.models.CalendarActivityType
import com.breastcancer.breastcancercare.models.SuitabilityDTO
import com.breastcancer.breastcancercare.screens.Route
import com.breastcancer.breastcancercare.theme.ColorSand
import com.breastcancer.breastcancercare.theme.ColorSunshine
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultSpacerSize
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.theme.OffBackground
import com.breastcancer.breastcancercare.utils.DefaultSpacer
import com.breastcancer.breastcancercare.utils.rememberIsLandscape
import com.breastcancer.breastcancercare.viewmodel.CalendarViewModel
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = koinViewModel(),
    bottomSpacer: Dp = DefaultSpacerSize,
    onAddNursing: () -> Unit,
    onAddCounselling: () -> Unit,
    onSubScreenChange: (Route, clearStack: Boolean) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(50) } // Adjust as needed
    val firstDayOfWeek =
        remember { DayOfWeek.SUNDAY /*firstDayOfWeekFromLocale()*/ } // Available from the library

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    val selectedTab by calendarViewModel.selectedTab.collectAsStateWithLifecycle()
    val selectedDate by calendarViewModel.selectedDate.collectAsStateWithLifecycle()
    val onDateClicked: (selectedDate: LocalDate) -> Unit = calendarViewModel::changeSelectedDate

    val selectedDayAvailableActivities by calendarViewModel.selectedDayAvailableActivities.collectAsStateWithLifecycle()

    val allDatesWithActivitiesAvailable by calendarViewModel.allDatesWithActivitiesAvailable.collectAsStateWithLifecycle()
    val allDatesWithActivitiesHistory by calendarViewModel.allDatesWithActivitiesHistory.collectAsStateWithLifecycle()

    val allSuitabilities by calendarViewModel.allSuitabilities.collectAsStateWithLifecycle()
    val selectedSuitability by calendarViewModel.selectedSuitability.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        VerticalCalendar(
            modifier = Modifier.background(OffBackground),
            state = state,
            dayContent = {
                var hasActivitiesAvailable by remember { mutableStateOf(false) }
                var hasActivitiesRegistered by remember { mutableStateOf(false) }
                LaunchedEffect(allDatesWithActivitiesAvailable) {
                    with(Dispatchers.IO) {
                        hasActivitiesAvailable =
                            allDatesWithActivitiesAvailable.contains(it.date.toString())
                        hasActivitiesRegistered =
                            allDatesWithActivitiesHistory.contains(it.date.toString())
                    }
                }
                if (it.position == DayPosition.MonthDate)
                    Day(
                        day = it,
                        selectedDate = selectedDate,
                        hasActivitiesAvailable = hasActivitiesAvailable,
                        hasActivitiesRegistered = hasActivitiesRegistered,
                        onDateClicked = onDateClicked
                    )
            },
            monthContainer = { month, container ->
                Column(modifier = Modifier.padding(vertical = DefaultVerticalPaddingMedium)) {
                    MonthHeader(month)
                    container.invoke()
                }
            },
            contentHeightMode = ContentHeightMode.Wrap,
            contentPadding = PaddingValues(
                horizontal = DefaultHorizontalPaddingSmall,
                vertical = DefaultVerticalPaddingMedium
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
                    horizontal = DefaultHorizontalPaddingSmall
                ).padding(bottom = DefaultVerticalPaddingMedium * 2).align(Alignment.TopCenter)
        ) {
            for (dayOfWeek in listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    text = dayOfWeek,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        BottomInfoCard(
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            selectedTab = selectedTab,
            selectedDayAvailableActivities = selectedDayAvailableActivities,
            bottomSpacer = bottomSpacer,
            selectedDate = selectedDate,
            onAddNursing = onAddNursing,
            onAddCounselling = onAddCounselling,
            onActivityClick = { activity ->
                onSubScreenChange(
                    if (activity.activityType is GeneralActivityType)
                        Route.Main.GeneralActivityDetail(type = activity.activityType.type)
                    else
                        Route.Main.ActivityDetail(id = activity.id),
                    false
                )
            }
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun Day(
    modifier: Modifier = Modifier,
    day: CalendarDay,
    selectedDate: LocalDate,
    hasActivitiesAvailable: Boolean,
    hasActivitiesRegistered: Boolean,
    onDateClicked: (selectedDate: LocalDate) -> Unit
) {
    val isLandscape = rememberIsLandscape()
    val currentDate by remember { mutableStateOf(LocalDate.now()) }
    val dayText: @Composable (selected: Boolean) -> Unit = { selected ->
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = if (selected) 1f else 0f),
                shape = CircleShape
            ).padding(5.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = day.date.day.toString(),
                color =
                    if (selected) MaterialTheme.colorScheme.onPrimary
                    else if (day.date == currentDate) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground,
                style =
                    LocalTextStyle.current.copy(fontWeight = if (day.date == currentDate || selected) FontWeight.Bold else FontWeight.Normal)
            )

            Row(
                modifier = Modifier.offset(y = 10.dp).align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                if (hasActivitiesAvailable)
                    Indicator()
                if (hasActivitiesRegistered)
                    Indicator(color = ColorSunshine)
            }
        }
    }
    Box(
        modifier = modifier
            .aspectRatio(if (isLandscape) 2f else 1f).clickable(
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
                true -> dayText(selected)
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
            color = MaterialTheme.colorScheme.tertiary
        )

    }
}

@Composable
fun BottomInfoCard(
    modifier: Modifier = Modifier,
    selectedTab: Int,
    openHeightFraction: Float = 0.73f,     // sheet height
    halfVisibleFraction: Float = 0.53f,  // visible part of the SHEET in Half state (0..1 of sheet height)
    peekVisibleFraction: Float = 0.23f, // visible part of the SCREEN in Peek state (0..1 of screen)
    initialValue: SheetValue = SheetValue.Half,
    velocityThresholdPx: Float = 1500f, // fling threshold
    selectedDate: LocalDate,
    bottomSpacer: Dp,
    selectedDayAvailableActivities: Map<CalendarActivityType, List<ActivityDTO>>,
    onAddNursing: () -> Unit,
    onAddCounselling: () -> Unit,
    onActivityClick: (activity: ActivityDTO) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }

    // Parent size needed to compute pixel offsets
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val parentH = with(density) { maxHeight.toPx() }.coerceAtLeast(1f)
        val sheetH = (parentH * openHeightFraction).coerceAtLeast(1f)

        // --- anchor offsets (from top of container) ---
        val open = 0f
        val half = (sheetH - sheetH * halfVisibleFraction).coerceIn(open, sheetH)
        val peek = (sheetH - parentH * peekVisibleFraction).coerceIn(open, sheetH)
        // ensure sorted order: Open <= Half <= Peek
        val anchors = remember(open, half, peek) {
            listOf(
                open,
                maxOf(half, open),
                maxOf(peek, maxOf(half, open))
            )
        }

        // initial offset based on initialValue
        val initialOffset = when (initialValue) {
            SheetValue.Open -> anchors[0]
            SheetValue.Half -> anchors[1]
            SheetValue.Peek -> anchors[2]
        }

        val offset = remember { Animatable(initialOffset) }

        // helper: closest/next/prev anchors
        fun nearestAnchor(x: Float): Float = anchors.minBy { abs(it - x) }
        fun nextAnchor(x: Float): Float = anchors.firstOrNull { it > x } ?: anchors.last()
        fun prevAnchor(x: Float): Float = anchors.lastOrNull { it < x } ?: anchors.first()

        suspend fun animateTo(target: Float) {
            offset.animateTo(
                target,
                spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
            )
        }

        // drag handling
        val dragState = rememberDraggableState { dy ->
            val newY = (offset.value + dy).coerceIn(anchors.first(), anchors.last())
            // snap during drag for responsiveness
            coroutineScope.launch {
                offset.snapTo(newY)
            }
        }

        // current state (optional to expose)
        val currentValue by remember {
            derivedStateOf {
                when (nearestAnchor(offset.value)) {
                    anchors[0] -> SheetValue.Open
                    anchors[1] -> SheetValue.Half
                    else -> SheetValue.Peek
                }
            }
        }

        Column(
            modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(openHeightFraction) // sheet height when fully open
            .offset { IntOffset(0, offset.value.roundToInt()) }
            .draggable(
                state = dragState,
                orientation = Orientation.Vertical,
                onDragStopped = { velocity ->
                    val v = velocity // +down, -up
                    val target = when {
                        v > velocityThresholdPx -> nextAnchor(offset.value) // fling down
                        v < -velocityThresholdPx -> prevAnchor(offset.value) // fling up
                        else -> nearestAnchor(offset.value)                   // snap to nearest
                    }
                    // If we flung “past” the nearest, ensure we move at least to the next/prev
                    // (nearestAnchor already handles gentle releases)
                    // animate to target anchor
                    // launch in composition scope:
                    coroutineScope.launch { animateTo(target) }
                })
        ) {
            Column(modifier = Modifier.height(50.dp)) {
                AnimatedVisibility(
                    visible = expanded,
                    enter = (
                            slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = spring()
                            )) + scaleIn(
                        transformOrigin = TransformOrigin(
                            0.9f,
                            1f
                        )
                    ) + fadeIn(),
                    exit = (
                            slideOutVertically(
                                targetOffsetY = { it / 2 },
                                animationSpec = spring()
                            )) + scaleOut(
                        transformOrigin = TransformOrigin(
                            0.9f,
                            1f
                        )
                    ) + fadeOut()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = DefaultHorizontalPaddingMedium),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onAddNursing,
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onTertiary,
                                containerColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Nurse,
                                    contentDescription = "Book a Nursing session"
                                )
                                Text(text = "Nursing")
                            }
                        }
                        DefaultSpacer(DefaultHorizontalPaddingSmall)
                        Button(
                            onClick = onAddCounselling,
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onTertiary,
                                containerColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Counselling,
                                    contentDescription = "Book a Counselling Session"
                                )
                                Text(text = "Counselling")
                            }
                        }
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                colors = CardDefaults.cardColors(containerColor = ColorSand)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = DefaultHorizontalPaddingSmall,
                            vertical = DefaultVerticalPaddingMedium
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val interaction = remember { MutableInteractionSource() }
                    Box(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
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
                                    coroutineScope.launch {
                                        animateTo(
                                            when (currentValue) {
                                                SheetValue.Open -> anchors[2]
                                                SheetValue.Half -> anchors[0]
                                                SheetValue.Peek -> anchors[1]
                                            }
                                        )
                                    }
                                }.semantics { role = Role.Button }
                        )
                        ExtendedFloatingActionButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            containerColor = MaterialTheme.colorScheme.primary,
                            expanded = !expanded,
                            elevation = FloatingActionButtonDefaults.loweredElevation(),
                            text = { Text(text = "Book", fontWeight = FontWeight.Bold) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.EditCalendar,
                                    contentDescription = "Book activity"
                                )
                            },
                            onClick = {
                                expanded = !expanded
                            })
                    }
                    ActivitySection(
                        modifier = Modifier.fillMaxSize(),
                        selectedDate = selectedDate,
                        selectedDayAvailableActivities = selectedDayAvailableActivities,
                        bottomSpacer = with(LocalDensity.current) { offset.value.toDp() + bottomSpacer },
                        onActivityClick = onActivityClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivitySection(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    selectedDayAvailableActivities: Map<CalendarActivityType, List<ActivityDTO>>,
    bottomSpacer: Dp,
    onActivityClick: (activity: ActivityDTO) -> Unit
) {
    AnimatedContent(selectedDayAvailableActivities, label = "Activities") { activities ->
        LazyColumnWithStickyFooter(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium),
            bottomSpacer = bottomSpacer,
            forceSpacer = true
        ) {
            if (activities.values.all { it.isEmpty() })
                item {
                    EmptyContainer(
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            else {
                activities.forEach { (type, activities) ->
                    if (activities.isNotEmpty())
                        stickyHeader {
                            Row(
                                modifier = Modifier.fillMaxWidth().background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            ColorSand,
                                            ColorSand.copy(0.9f),
                                            ColorSand.copy(alpha = 0f)
                                        )
                                    )
                                ).padding(vertical = DefaultVerticalPaddingSmall),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    DefaultHorizontalPaddingSmall
                                )
                            ) {
                                Indicator(
                                    size = 14.dp,
                                    color = if (type == CalendarActivityType.Registered) ColorSunshine else MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = if (type == CalendarActivityType.Registered) "Your registered activities on this day" else "Activities available to you on this day",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = if (type == CalendarActivityType.Registered) ColorSunshine else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    items(activities) { activity ->
                        ActivityDesign(
                            modifier = Modifier.fillMaxWidth(),
                            selectedDate = selectedDate,
                            activityDTO = activity,
                            onClick = {
                                onActivityClick(activity)
                            })
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyContainer(modifier: Modifier) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().alpha(0.5f)
                .padding(
                    horizontal = DefaultHorizontalPaddingSmall,
                    vertical = DefaultVerticalPaddingMedium * 2
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium)
        ) {
            Icon(imageVector = Icons.Outlined.HourglassEmpty, contentDescription = "Empty Icon")
            Text(
                text = "Oops! \nIt seems that there are no activities registered for today.",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Indicator(size: Dp = 7.dp, color: Color = MaterialTheme.colorScheme.primary) = Box(
    modifier = Modifier.size(size).background(
        color = color,
        shape = CircleShape
    )
)


enum class SheetValue { Open, Half, Peek }