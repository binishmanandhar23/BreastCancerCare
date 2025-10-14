package com.breastcancer.breastcancercare.models

import kotlinx.datetime.LocalDate

data class GeneralActivityDTO(
    val activityDTO: ActivityDTO?,
    val activityScheduleDTO: List<ActivityScheduleDTO> = emptyList(),
    val appointmentDate: LocalDate? = null
)
