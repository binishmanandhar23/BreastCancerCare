package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.AllListContainer
import com.breastcancer.breastcancercare.components.BreastCancerCircularLoader
import com.breastcancer.breastcancercare.components.CategoryChip
import com.breastcancer.breastcancercare.components.CoreHomeCardDesign
import com.breastcancer.breastcancercare.components.TimeAndDateFormat
import com.breastcancer.breastcancercare.components.UrlImage
import com.breastcancer.breastcancercare.database.local.types.ActivityUtils
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.screens.Route
import com.breastcancer.breastcancercare.states.ActivityUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.viewmodel.ActivityViewModel

@Composable
fun AllActivitiesScreen(
    activityViewModel: ActivityViewModel,
    onBackPress: () -> Unit,
    onSubScreenChange: (Route) -> Unit
) {
    val activityUIListState by activityViewModel.activityUIListState.collectAsStateWithLifecycle()
    val allActivityTypes by activityViewModel.allActivityTypes.collectAsStateWithLifecycle()
    val selectedActivityType by activityViewModel.selectedActivityType.collectAsStateWithLifecycle()
    AllListContainer(
        title = "Activities",
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
            when (activityUIListState) {
                is ActivityUIState.Loading -> item { BreastCancerCircularLoader() }
                is ActivityUIState.Success -> items(
                    items = activityUIListState.data ?: emptyList(),
                    key = { activity -> activity.id }
                ) { activity ->
                    ActivityCard(
                        modifier = Modifier.fillMaxWidth().height(350.dp)
                            .padding(horizontal = DefaultHorizontalPaddingMedium),
                        activity = activity,
                        onClick = {
                            onSubScreenChange(Route.Main.ActivityDetail(it.id))
                        })
                }

                else -> Unit
            }
        }
    )
}

@Composable
private fun ActivityCard(
    modifier: Modifier,
    activity: ActivityDTO,
    onClick: (activity: ActivityDTO) -> Unit
) =
    CoreHomeCardDesign(
        onClick = { onClick(activity) },
        modifier = modifier, image = {
            UrlImage(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                url = activity.image ?: "",
            )
        }, title = {
            Column(verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingSmall)) {
                TimeAndDateFormat(activityDTO = activity, selectedDate = activity.startDate)
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }, subtitle = {
            Text(
                text = activity.description,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        })