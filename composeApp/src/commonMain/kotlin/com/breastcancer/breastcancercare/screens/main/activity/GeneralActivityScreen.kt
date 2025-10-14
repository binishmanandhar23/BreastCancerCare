package com.breastcancer.breastcancercare.screens.main.activity

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.BreastCancerButton
import com.breastcancer.breastcancercare.components.BreastCancerCircularLoader
import com.breastcancer.breastcancercare.components.appDateFormat
import com.breastcancer.breastcancercare.components.date.DatePickerModal
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthLong
import com.breastcancer.breastcancercare.components.snackbar.SnackBarLengthMedium
import com.breastcancer.breastcancercare.components.snackbar.SnackBarState
import com.breastcancer.breastcancercare.database.local.types.ActivityType
import com.breastcancer.breastcancercare.database.local.types.GeneralActivityType
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.models.ActivityScheduleDTO
import com.breastcancer.breastcancercare.states.ActivityUIState
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultHorizontalPaddingSmall
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import com.breastcancer.breastcancercare.utils.convertMillisToLocalDate
import com.breastcancer.breastcancercare.viewmodel.ActivityViewModel
import kotlinx.datetime.LocalDate

@Composable
fun GeneralActivityScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    activityType: ActivityType,
    customSnackBarState: SnackBarState,
    loaderState: LoaderState,
    activityViewModel: ActivityViewModel,
    onBack: () -> Unit,
    onBookAppointment: (activity: ActivityDTO, activityHistories: List<ActivityScheduleDTO>, appointmentDate: LocalDate) -> Unit
) {
    val activityUIState by activityViewModel.activityUIAddState.collectAsStateWithLifecycle()
    val activityHistoryDates by remember(activityUIState) {
        derivedStateOf {
            val dates = mutableListOf<LocalDate>()
            if (activityUIState is ActivityUIState.Success)
                dates.addAll(activityUIState.data?.activityScheduleDTO?.map { it.registeredForDate }
                    ?: emptyList())
            dates
        }
    }
    var showModal by remember { mutableStateOf(false) }
    LaunchedEffect(activityType) {
        activityViewModel.getActivityByType(type = activityType)
    }
    LaunchedEffect(activityUIState) {
        if(activityUIState is ActivityUIState.Loading)
            loaderState.show()
        else
            loaderState.hide()

        when (activityUIState) {
            is ActivityUIState.Success -> {
                (activityUIState as ActivityUIState.Success).let { success ->
                    if(success.registrationUIState is ActivityUIState.Success.RegistrationUIState.Registered)
                        success.data?.appointmentDate?.let {
                            customSnackBarState.show(
                                overridingText = "Activity booked on ${
                                    appDateFormat(
                                        date = it
                                    )
                                } successfully", overridingDelay = SnackBarLengthLong
                            )
                            onBack()
                        }
                }
            }

            else -> Unit
        }
    }
    AnimatedContent(modifier = Modifier.fillMaxSize(), targetState = activityUIState) { state ->
        when (state) {
            is ActivityUIState.Success -> {
                val activity by remember { derivedStateOf { state.data?.activityDTO } }
                val activityHistories by remember { derivedStateOf { state.data?.activityScheduleDTO } }
                val appointmentDate by remember { derivedStateOf { state.data?.appointmentDate } }
                ActivityOuterContainer(
                    modifier = modifier,
                    activity = activity,
                    onBackClick = onBack,
                    bottomBar = {
                        when(activityUIState.registrationUIState){
                            is ActivityUIState.Success.RegistrationUIState.Registering -> BreastCancerCircularLoader(
                                Modifier.align(
                                    Alignment.CenterEnd
                                ), size = 30.dp
                            )
                            else -> BreastCancerButton(
                                modifier = Modifier.align(Alignment.CenterEnd)
                                    .padding(vertical = DefaultVerticalPaddingSmall),
                                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                text = when (activity?.activityType) {
                                    is GeneralActivityType.Counselling,
                                    is GeneralActivityType.Nursing -> "Book Appointment"

                                    is GeneralActivityType.FinancialAndPracticalHardshipSupport -> "Enquire"
                                    else -> ""
                                }, enabled = appointmentDate != null, onDisabledClick = {
                                    customSnackBarState.show(
                                        overridingText = "Please select an appointment date",
                                        overridingDelay = SnackBarLengthMedium
                                    )
                                }, onClick = {
                                    if (appointmentDate != null && activity != null && activityHistories != null)
                                        onBookAppointment(activity!!,activityHistories!!, appointmentDate!!)
                                })
                        }
                    },
                    bodyContent = {
                        ActivityBodyContainer(activity = activity, extraContent = {
                            if (activityHistoryDates.isNotEmpty())
                                BookedDatesSection(dates = activityHistoryDates)
                            NewAppointmentDateSection(
                                appointmentDate = appointmentDate,
                                onShowModal = { showModal = true }
                            )
                        })
                    })
            }

            else -> Unit
        }
    }
    if (showModal)
        DatePickerModal(onDateSelected = { dateInMillis ->
            dateInMillis?.let {
                if (activityHistoryDates.contains(convertMillisToLocalDate(millis = it))) {
                    customSnackBarState.show(
                        overridingText = "This date has already been booked.",
                        overridingDelay = SnackBarLengthMedium
                    )
                    return@DatePickerModal
                }
            }
            activityViewModel.updateBookingAppointment(dateInMillis = dateInMillis)
        }, onDismiss = {
            showModal = false
        })
}

@Composable
private fun NewAppointmentDateSection(
    appointmentDate: LocalDate?,
    onShowModal: () -> Unit
) {
    CardContainer(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPaddingMedium)
    ) {
        ColumnContainer(title = "New Appointment Date") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (appointmentDate == null) "No Date Selected" else appDateFormat(
                        date = appointmentDate,
                        includeYear = true
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
                FloatingActionButton(
                    onClick = onShowModal,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Date"
                    )
                }
            }
        }
    }
}

@Composable
private fun BookedDatesSection(dates: List<LocalDate>) {
    CardContainer(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPaddingMedium)
    ) {
        ColumnContainer(title = "Your Booked Dates") {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingMedium)
            ) {
                dates.forEach { date ->
                    Text(
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        )
                            .padding(
                                horizontal = DefaultHorizontalPaddingSmall,
                                vertical = DefaultVerticalPaddingSmall
                            ),
                        text = appDateFormat(date = date, includeYear = true),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}