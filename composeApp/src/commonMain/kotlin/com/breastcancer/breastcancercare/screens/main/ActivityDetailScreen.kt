package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.CategoryChip
import com.breastcancer.breastcancercare.components.UrlImage
import com.breastcancer.breastcancercare.database.local.types.ActivityType
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.states.ActivityUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
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
                            verticalArrangement = object : Arrangement.Vertical {
                                override fun Density.arrange(
                                    totalSize: Int,
                                    sizes: IntArray,
                                    outPositions: IntArray
                                ) {
                                    var y = 0
                                    for (i in sizes.indices) {
                                        outPositions[i] = y
                                        y += sizes[i]
                                        if (i != sizes.lastIndex) y += DefaultVerticalPaddingSmall.roundToPx()
                                    }
                                    if (y < totalSize)
                                        outPositions.lastIndex.let {
                                            outPositions[it] = totalSize - sizes.last()
                                        }
                                }
                            }
                        ) {
                            Text(
                                text = activity?.title ?: "",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            activity?.category?.let {
                                CategoryChip(categoryName = UserCategory.getLabel(it))
                            }
                            HorizontalDivider(modifier = Modifier.fillMaxWidth())
                            Text(
                                text = activity?.description ?: "",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Box(modifier = Modifier.fillMaxWidth().padding(top = DefaultVerticalPaddingLarge)) {
                                BreastCancerButton(
                                    modifier = Modifier.align(Alignment.Center),
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
                }
            }

            else -> Unit
        }
    }
}