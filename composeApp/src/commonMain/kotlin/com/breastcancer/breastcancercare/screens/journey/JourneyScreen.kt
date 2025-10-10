package com.breastcancer.breastcancercare.screens.journey

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.BreastCancerBackButton
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.icons.Digital_wellbeing
import com.breastcancer.breastcancercare.components.icons.Stars
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.states.LoginUIState
import com.breastcancer.breastcancercare.theme.ColorSunshine
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.utils.rememberIsLandscape
import com.breastcancer.breastcancercare.viewmodel.OnboardingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun JourneyScreen(
    userId: Long,
    userCategory: UserCategory? = null,
    customSnackBarState: SnackBarState,
    hideBackButton: Boolean,
    onNext: (userId: Long, userCategory: UserCategory) -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    var selected by remember(userCategory) { mutableStateOf(userCategory) }
    val isLandscape = rememberIsLandscape()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(
                horizontal = DefaultHorizontalPaddingMedium,
                vertical = DefaultVerticalPaddingLarge + DefaultVerticalPaddingLarge
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
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
                        if (i != sizes.lastIndex) y += DefaultHorizontalPaddingMedium.roundToPx()
                    }
                    if (y < totalSize)
                        outPositions.lastIndex.let {
                            outPositions[it] = totalSize - sizes.last()
                        }
                }
            }
        ) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(bottom = DefaultVerticalPaddingMedium),
                text = "Choose your User Journey",
                style = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            val content: @Composable (modifier: Modifier) -> Unit = { modifier ->
                CardSelectorDesign(
                    modifier = modifier,
                    imageVector = Stars,
                    userCategory = UserCategory.StartingStrong,
                    selected = selected == UserCategory.StartingStrong,
                    onClick = {
                        selected = it
                    })
                CardSelectorDesign(
                    modifier = modifier,
                    imageVector = Digital_wellbeing,
                    userCategory = UserCategory.LivingWell,
                    selected = selected == UserCategory.LivingWell,
                    onClick = {
                        selected = it
                    })
            }
            if (isLandscape)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DefaultHorizontalPaddingMedium),
                    horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalPaddingLarge),
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        content(Modifier.weight(0.5f))
                    }
                )
            else
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DefaultHorizontalPaddingMedium),
                    verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingLarge),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        content(Modifier.fillMaxWidth())
                    }
                )
            BreastCancerButton(
                text = "Learn More",
                onClick = {
                    onNext(userId, selected!!)
                },
                enabled = selected != null, onDisabledClick = {
                    customSnackBarState.show(
                        overridingText = "Select one of the journey above before proceeding.",
                        overridingDelay = SnackBarLengthMedium
                    )
                }
            )
        }
        if (!hideBackButton)
            BreastCancerBackButton(onBackClick = onBack)
    }
}

@Composable
private fun CardSelectorDesign(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    userCategory: UserCategory,
    selected: Boolean,
    onClick: (userCategory: UserCategory) -> Unit
) {
    val textColor by animateColorAsState(if (selected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.tertiary)
    val backgroundColor by animateColorAsState(if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onTertiary)
    val elevation by animateDpAsState(if (selected) 50.dp else 1.dp)
    val scale by animateFloatAsState(if (selected) 1f else 0.9f)
    Card(
        modifier = modifier.scale(scale),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        onClick = {
            onClick(userCategory)
        }
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = DefaultHorizontalPaddingMedium,
                vertical = DefaultVerticalPaddingLarge + DefaultVerticalPaddingLarge
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                DefaultHorizontalPaddingLarge
            )
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = imageVector,
                contentDescription = UserCategory.getLabel(userCategory),
                tint = ColorSunshine
            )
            Text(
                text = UserCategory.getLabel(userCategory),
                color = textColor,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}