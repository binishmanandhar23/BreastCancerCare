package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.CategoryChip
import com.breastcancer.breastcancercare.components.SeeMoreComponent
import com.breastcancer.breastcancercare.components.UrlImage
import com.breastcancer.breastcancercare.database.local.types.ActivityType
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.states.ActivityUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.theme.InfoAnim
import com.breastcancer.breastcancercare.utils.OverlappingZoomHeaderWithParallax
import com.breastcancer.breastcancercare.viewmodel.ActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ActivityDetailScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    activityViewModel: ActivityViewModel,
    id: Long,
    onBack: () -> Unit
) {
    val activityUIState by activityViewModel.activityUIDetailState.collectAsStateWithLifecycle()
    LaunchedEffect(id) {
        withContext(Dispatchers.Main) {
            activityViewModel.getActivityById(id = id)
        }
    }
    AnimatedContent(modifier = Modifier.fillMaxSize(), targetState = activityUIState) { state ->
        when (state) {
            is ActivityUIState.Success -> {
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

                                            is StartingStrongActivityType.Counselling,
                                            is StartingStrongActivityType.Nursing,
                                            is LivingWellActivityType.Counselling,
                                            is LivingWellActivityType.Nursing -> "book an appointment"

                                            is StartingStrongActivityType.FinancialAndPracticalHardshipSupport,
                                            is LivingWellActivityType.FinancialAndPracticalHardshipSupport -> "enquire"

                                            else -> ""
                                        },
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                                Box(modifier = Modifier.weight(0.5f)) {
                                    BreastCancerButton(
                                        modifier = Modifier.align(Alignment.Center)
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

                                            is StartingStrongActivityType.Counselling,
                                            is StartingStrongActivityType.Nursing,
                                            is LivingWellActivityType.Counselling,
                                            is LivingWellActivityType.Nursing -> "Book Appointment"

                                            is StartingStrongActivityType.FinancialAndPracticalHardshipSupport,
                                            is LivingWellActivityType.FinancialAndPracticalHardshipSupport -> "Enquire"

                                            else -> ""
                                        }, onClick = {

                                        })
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
                                horizontal = DefaultHorizontalPaddingMedium,
                                vertical = DefaultVerticalPaddingLarge
                            ),
                            verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingSmall)
                        ) {
                            Text(
                                text = activity?.title ?: "",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            activity?.category?.let {
                                CategoryChip(categoryName = UserCategory.getLabel(it))
                            }
                            DescriptionSection(activity = activity)
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
    CardContainer(
        modifier = Modifier.fillMaxWidth().animateContentSize(
            animationSpec = tween(
                durationMillis = InfoAnim.Expand,
                easing = LinearEasing
            )
        )
    ) {
        ColumnContainer(modifier = Modifier.clickable{
            isExpanded = !isExpanded
        }, title = "Description") {
            Text(
                text = activity?.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )
            SeeMoreComponent(isExpanded = isExpanded)
        }
    }
}

@Composable
private fun CardContainer(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    colors: CardColors = CardDefaults.cardColors(containerColor = containerColor),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    if (onClick == null)
        Card(
            modifier = modifier,
            colors = colors,
            elevation = elevation
        ) {
            content()
        }
    else
        Card(
            modifier = modifier,
            colors = colors,
            elevation = elevation,
            onClick = onClick
        ) {
            content()
        }
}

@Composable
private fun ColumnContainer(
    modifier: Modifier,
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