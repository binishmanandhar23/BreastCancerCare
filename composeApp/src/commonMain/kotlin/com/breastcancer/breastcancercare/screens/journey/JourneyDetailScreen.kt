package com.breastcancer.breastcancercare.screens.journey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.components.BreastCancerToolbar
import com.breastcancer.breastcancercare.components.LazyColumnWithStickyFooter
import com.breastcancer.breastcancercare.components.icons.Digital_wellbeing
import com.breastcancer.breastcancercare.components.icons.Stars
import com.breastcancer.breastcancercare.components.slider.SlideToProceed
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.theme.ColorOnSunshine
import com.breastcancer.breastcancercare.theme.ColorSunshine
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.utils.text.LoremIpsum
import com.breastcancer.breastcancercare.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun JourneyDetailScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    userId: Long,
    userCategory: UserCategory,
    customSnackBarState: SnackBarState,
    onBack: () -> Unit,
    onJourneyComplete: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val icon =
        remember(userCategory) { if (userCategory == UserCategory.StartingStrong) Stars else Digital_wellbeing }
    LazyColumnWithStickyFooter(
        modifier = Modifier.fillMaxSize(),
        forceSpacer = true,
        stickyFooter = {
            SlideToProceed(
                modifier = Modifier.padding(
                    horizontal = DefaultHorizontalPaddingLarge,
                    vertical = DefaultVerticalPaddingLarge
                ),
                text = "Yes! I'm ${UserCategory.getLabel(category = userCategory)}",
                icon = icon,
                iconTint = ColorOnSunshine,
                thumbColor = ColorSunshine,
                onClick = {
                    customSnackBarState.show(
                        overridingText = "Slide to proceed",
                        overridingDelay = SnackBarLengthMedium
                    )
                },
                onComplete = {
                    coroutineScope.launch {
                        homeViewModel.updateUserCategoryById(
                            userId = userId,
                            userCategory = userCategory
                        )
                        onJourneyComplete()
                    }
                })
        }) {
        stickyHeader {
            BreastCancerToolbar(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background.copy(0.8f)
                            )
                        )
                    ),
                title = UserCategory.getLabel(category = userCategory),
                onBack = onBack
            )
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = DefaultHorizontalPaddingMedium,
                    vertical = DefaultVerticalPaddingMedium
                ),
                verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingLarge)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        modifier = Modifier.size(80.dp).align(Alignment.Center),
                        imageVector = icon,
                        contentDescription = UserCategory.getLabel(category = userCategory),
                        tint = ColorSunshine
                    )
                }
                Text(text = LoremIpsum, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}