package com.breastcancer.breastcancercare.screens.main.activity

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.ActivityTypeTag
import com.breastcancer.breastcancercare.components.AllListContainer
import com.breastcancer.breastcancercare.components.BreastCancerCircularLoader
import com.breastcancer.breastcancercare.components.CategoryChip
import com.breastcancer.breastcancercare.components.UrlImage
import com.breastcancer.breastcancercare.components.UserCategoryTag
import com.breastcancer.breastcancercare.database.local.types.ActivityUtils
import com.breastcancer.breastcancercare.models.ActivityHistoryDTO
import com.breastcancer.breastcancercare.screens.Route
import com.breastcancer.breastcancercare.states.ActivityUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.viewmodel.ActivityViewModel

@Composable
fun ActivityHistoryScreen(
    activityViewModel: ActivityViewModel, onBackPress: () -> Unit,
    onSubScreenChange: (Route) -> Unit
) {
    val allActivityTypes by activityViewModel.allActivityTypes.collectAsStateWithLifecycle()
    val selectedActivityType by activityViewModel.selectedActivityType.collectAsStateWithLifecycle()
    val activityUIHistoryState by activityViewModel.activityUIHistoryState.collectAsStateWithLifecycle()
    AllListContainer(
        title = "History",
        listOfCategories = allActivityTypes,
        selectedCategory = selectedActivityType,
        categorySectionContent = { borderStroke ->
            items(items = allActivityTypes) { activityType ->
                val selectedContainerColor by animateColorAsState(if (selectedActivityType == activityType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                val selectedContentColor by animateColorAsState(if (selectedActivityType == activityType) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground)
                CategoryChip(
                    categoryName = ActivityUtils.getActivityTypeLabel(activityType),
                    border = borderStroke,
                    colors = CardDefaults.cardColors(
                        containerColor = selectedContainerColor,
                        contentColor = selectedContentColor
                    ), onClick = { activityViewModel.selectActivityType(activityType) }
                )
            }
        },
        onBack = onBackPress,
        onAllClicked = { activityViewModel.selectActivityType(null) },
        content = {
            when (activityUIHistoryState) {
                is ActivityUIState.Loading -> item { BreastCancerCircularLoader() }
                is ActivityUIState.Success -> items(
                    items = activityUIHistoryState.data ?: emptyList(),
                    key = { activityHistory -> activityHistory.id ?: 0 }
                ) { activity ->
                    ActivityHistoryCard(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = DefaultHorizontalPaddingMedium),
                        activityHistory = activity,
                        onClick = {

                        })
                }

                else -> Unit
            }
        }
    )
}

@Composable
private fun ActivityHistoryCard(
    modifier: Modifier = Modifier,
    activityHistory: ActivityHistoryDTO,
    onClick: (ActivityHistoryDTO) -> Unit
) {
    val activity by remember(activityHistory) { mutableStateOf(activityHistory.activity) }
    activity?.let { activity ->
        Card(modifier = modifier, onClick = {
            onClick(activityHistory)
        }) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = DefaultHorizontalPaddingMedium,
                    vertical = DefaultVerticalPaddingMedium
                ), verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium)
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(
                        DefaultHorizontalPaddingSmall
                    ), verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingSmall)
                ) {
                    UserCategoryTag(
                        modifier = Modifier.scale(0.8f),
                        userCategory = activity.category
                    )
                    ActivityTypeTag(
                        modifier = Modifier.scale(0.8f),
                        activityType = activity.activityType
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(0.7f)) {
                        Text(
                            text = activity.title,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = activity.description,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    UrlImage(
                        modifier = Modifier.weight(0.3f).clip(MaterialTheme.shapes.medium),
                        url = activity.image ?: "",
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}