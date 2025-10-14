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
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
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
    customSnackBarState: SnackBarState,
    loaderState: LoaderState,
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
    LaunchedEffect(activityUIState){
        if(activityUIState is ActivityUIState.Loading)
            loaderState.show()
        else
            loaderState.hide()

        if(activityUIState is ActivityUIState.Success)
            if(activityUIState.registrationUIState is ActivityUIState.Success.RegistrationUIState.Registered)
                customSnackBarState.show(overridingText = "Registered Successfully", overridingDelay = SnackBarLengthMedium)
    }
    AnimatedContent(modifier = Modifier.fillMaxSize(), targetState = activityUIState) { state ->
        when (state) {
            is ActivityUIState.Success -> {
                val activity by remember { derivedStateOf { state.data } }
                ActivityOuterContainer(
                    modifier = modifier,
                    activity = activity,
                    onBackClick = onBack,
                    bottomBar = {
                        when (activityUIState.registrationUIState) {
                            is ActivityUIState.Success.RegistrationUIState.Registering -> BreastCancerCircularLoader(
                                Modifier.align(
                                    Alignment.CenterEnd
                                ), size = 30.dp
                            )
                            is ActivityUIState.Success.RegistrationUIState.Initial -> BreastCancerButton(
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

                            is ActivityUIState.Success.RegistrationUIState.Registered -> Text(
                                modifier = Modifier.align(Alignment.CenterEnd)
                                    .padding(vertical = DefaultVerticalPaddingSmall),
                                text = "Registered ✔",
                                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                color = MaterialTheme.colorScheme.primary
                            )
                            else -> Unit
                        }
                    },
                    bodyContent = {
                        ActivityBodyContainer(activity = activity)
                    })
            }

            else -> Unit
        }
    }
}

