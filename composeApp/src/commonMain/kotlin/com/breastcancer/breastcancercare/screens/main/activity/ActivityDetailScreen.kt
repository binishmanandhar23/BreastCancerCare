package com.breastcancer.breastcancercare.screens.main.activity

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.ActivityTypeTag
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.BreastCancerCircularLoader
import com.breastcancer.breastcancercare.components.SeeMoreComponent
import com.breastcancer.breastcancercare.components.TimeAndDateFormat
import com.breastcancer.breastcancercare.components.UrlImage
import com.breastcancer.breastcancercare.components.UserCategoryTag
import com.breastcancer.breastcancercare.components.icons.User
import com.breastcancer.breastcancercare.database.local.types.GeneralActivityType
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.states.ActivityUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.theme.InfoAnim
import com.breastcancer.breastcancercare.utils.DefaultSpacer
import com.breastcancer.breastcancercare.utils.OverlappingZoomHeaderWithParallax
import com.breastcancer.breastcancercare.utils.getDateForNextSession
import com.breastcancer.breastcancercare.utils.text.LinkText
import com.breastcancer.breastcancercare.viewmodel.ActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ActivityDetailScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    activityViewModel: ActivityViewModel,
    id: Long,
    onBack: () -> Unit,
    onRegister: (activity: ActivityDTO) -> Unit
) {
    val activityUIState by activityViewModel.activityUIDetailState.collectAsStateWithLifecycle()
    LaunchedEffect(id) {
        withContext(Dispatchers.Main) {
            activityViewModel.getActivityById(id = id)
        }
    }
    AnimatedContent(modifier = Modifier.fillMaxSize(), targetState = activityUIState) { state ->
        when (state) {
            is ActivityUIState.Loading -> BreastCancerCircularLoader(
                modifier = Modifier.fillMaxSize(),
                size = 40.dp
            )

            is ActivityUIState.Success, is ActivityUIState.Final -> {
                val activity by remember { derivedStateOf { state.data } }
                OverlappingZoomHeaderWithParallax(
                    modifier = modifier,
                    header = {
                        UrlImage(
                            modifier = it,
                            url = activity?.image ?: "",
                            contentDescription = activity?.title
                        )
                    },
                    stickyFooter = {
                        CardContainer(
                            modifier = Modifier.fillMaxWidth()
                                .padding(
                                    vertical = DefaultVerticalPaddingLarge,
                                    horizontal = DefaultHorizontalPaddingMedium
                                ),
                            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(
                                    horizontal = DefaultHorizontalPaddingMedium,
                                    vertical = DefaultVerticalPaddingSmall
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(0.5f),
                                    verticalArrangement = Arrangement.spacedBy(
                                        DefaultVerticalPaddingSmall
                                    )
                                ) {
                                    Text(
                                        "How to join",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Text(
                                        text = "Tap the button to " + when (activity?.activityType) {
                                            is StartingStrongActivityType.SupportGroups,
                                            is StartingStrongActivityType.Workshops,
                                            is LivingWellActivityType.DiscussionGroups,
                                            is LivingWellActivityType.Workshops,
                                            is LivingWellActivityType.Webinars,
                                            is LivingWellActivityType.WellnessActivities,
                                            is LivingWellActivityType.MindfulRecoveryProgram -> "register your interest"

                                            is GeneralActivityType.Counselling,
                                            is GeneralActivityType.Nursing -> "book an appointment"

                                            is GeneralActivityType.FinancialAndPracticalHardshipSupport-> "enquire"

                                            else -> ""
                                        },
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                                Box(modifier = Modifier.weight(0.5f)) {
                                    if (activityUIState is ActivityUIState.Success)
                                        BreastCancerButton(
                                            modifier = Modifier.align(Alignment.CenterEnd)
                                                .padding(vertical = DefaultVerticalPaddingSmall),
                                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                            text = when (activity?.activityType) {
                                                is StartingStrongActivityType.SupportGroups,
                                                is StartingStrongActivityType.Workshops,
                                                is LivingWellActivityType.DiscussionGroups,
                                                is LivingWellActivityType.Workshops,
                                                is LivingWellActivityType.Webinars,
                                                is LivingWellActivityType.WellnessActivities,
                                                is LivingWellActivityType.MindfulRecoveryProgram -> "Register Interest"

                                                is GeneralActivityType.Counselling,
                                                is GeneralActivityType.Nursing -> "Book Appointment"

                                                is GeneralActivityType.FinancialAndPracticalHardshipSupport -> "Enquire"

                                                else -> ""
                                            }, onClick = {
                                                activity?.let {
                                                    onRegister(it)
                                                }
                                            })
                                    else if (activityUIState is ActivityUIState.Final)
                                        Text(
                                            modifier = Modifier.align(Alignment.CenterEnd)
                                                .padding(vertical = DefaultVerticalPaddingSmall),
                                            text = "Registered ✔",
                                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                }
                            }
                        }
                    },
                    onBackClick = onBack
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(
                                vertical = DefaultVerticalPaddingLarge
                            ),
                            verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = DefaultHorizontalPaddingMedium),
                                verticalArrangement = Arrangement.spacedBy(
                                    DefaultVerticalPaddingSmall
                                )
                            ) {
                                Text(
                                    text = activity?.title ?: "",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        DefaultHorizontalPaddingSmall
                                    )
                                ) {
                                    activity?.category?.let {
                                        UserCategoryTag(userCategory = it)
                                    }
                                    activity?.activityType?.let { activityType ->
                                        ActivityTypeTag(activityType = activityType)
                                    }
                                }
                            }
                            DescriptionSection(activity = activity)
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = DefaultHorizontalPaddingMedium).height(
                                        IntrinsicSize.Min
                                    )
                            ) {
                                activity?.let { activity ->
                                    if (!activity.audience.isNullOrEmpty()) {
                                        CardContainer(
                                            modifier = Modifier.weight(0.5f).fillMaxHeight()
                                        ) {
                                            ColumnContainer(title = "Who it's for") {
                                                Text(
                                                    text = activity.audience,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                        DefaultSpacer(size = DefaultHorizontalPaddingSmall)
                                    }
                                    if (activity.startDate != null)
                                        getDateForNextSession(
                                            frequencyType = activity.frequency,
                                            frequencySeries = activity.frequencySeries,
                                            startDate = activity.startDate,
                                            endDate = activity.endDate
                                        )?.let { nextSession ->
                                            CardContainer(
                                                modifier = Modifier.weight(0.5f).fillMaxHeight()
                                            ) {
                                                ColumnContainer(title = "When") {
                                                    Text(
                                                        text = "Next Session",
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                    TimeAndDateFormat(
                                                        activityDTO = activity,
                                                        selectedDate = nextSession
                                                    )
                                                    Text(
                                                        text = "Repeats",
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                    Text(
                                                        text = activity.frequency.type,
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }
                                            }
                                        }
                                }
                            }
                            WhereSection(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = DefaultHorizontalPaddingMedium),
                                activity = activity
                            )
                        }
                    }
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun DescriptionSection(activity: ActivityDTO?) {
    var isExpanded by remember { mutableStateOf(false) }
    var isTruncated by remember { mutableStateOf(false) }
    CardContainer(
        modifier = Modifier.fillMaxWidth().animateContentSize(
            animationSpec = tween(
                durationMillis = InfoAnim.Expand,
                easing = LinearEasing
            )
        ).padding(
            horizontal = DefaultHorizontalPaddingMedium,
            vertical = DefaultVerticalPaddingSmall
        )
    ) {
        ColumnContainer(
            modifier = Modifier.clickable {
                isExpanded = !isExpanded
            }, title = "Description"
        ) {
            Text(
                text = activity?.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { result ->
                    // When collapsed (maxLines=3), this is the signal that the text needed > 3 lines
                    val truncated = !isExpanded && result.hasVisualOverflow

                    // (Optional) when expanded you can directly check lineCount > 3
                    val moreThan3 = isExpanded && result.lineCount > 3

                    val shouldShowSeeMore = truncated || moreThan3
                    if (isTruncated != shouldShowSeeMore) isTruncated = shouldShowSeeMore
                }
            )
            if (isTruncated)
                SeeMoreComponent(isExpanded = isExpanded)
        }
    }
}

@Composable
private fun WhereSection(modifier: Modifier = Modifier, activity: ActivityDTO?) {
    if (activity == null)
        return
    CardContainer(modifier = modifier) {
        ColumnContainer(title = "Where") {
            if (activity.isOnline) {
                Text(
                    text = "Online",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                LinkText(url = activity.onlineLink ?: "")
            } else {
                activity.location?.let { location ->
                    Text(
                        text = "${location.suburb} - ${location.state}, ${location.country}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Composable
private fun CardContainer(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    colors: CardColors = CardDefaults.cardColors(containerColor = containerColor),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        colors = colors,
        elevation = elevation
    ) {
        content()
    }
}

@Composable
private fun ColumnContainer(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.padding(
            horizontal = DefaultHorizontalPaddingMedium,
            vertical = DefaultVerticalPaddingSmall
        ), verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingSmall)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        content()
    }
}