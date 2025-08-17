package com.breastcancer.breastcancercare.models.interfaces

import com.breastcancer.breastcancercare.database.local.types.EventType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface ProgramEventDTO {
    val id: Long
    val name: String
    val description: String
    val date: LocalDate
    val startTime: LocalTime?
    val endTime: LocalTime?
    val location: String?
    val eventType: EventType
    val isOnline: Boolean
}