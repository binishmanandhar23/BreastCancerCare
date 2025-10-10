package com.breastcancer.breastcancercare.models

import kotlinx.datetime.LocalDate

data class GeneralActivityDTO(
    val activityDTO: ActivityDTO?,
    val activityHistoryDTO: List<ActivityHistoryDTO> = emptyList(),
    val appointmentDate: LocalDate? = null
)
