package com.breastcancer.breastcancercare.models.interfaces

import com.breastcancer.breastcancercare.database.local.entity.ProgramEntity
import com.breastcancer.breastcancercare.database.local.types.EventType
import com.breastcancer.breastcancercare.database.local.types.FrequencyType
import com.breastcancer.breastcancercare.models.SuitabilityDTO
import com.breastcancer.breastcancercare.models.toSuitabilityDTO
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ProgramDTO(
    override val id: Long,
    override val name: String,
    override val description: String,
    override val startTime: LocalTime?,
    override val endTime: LocalTime?,
    override val location: String?,
    override val eventType: EventType,
    override val isOnline: Boolean,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val frequency: FrequencyType,
    val suitability: List<SuitabilityDTO>
) : ProgramEventDTO

fun ProgramEntity.toProgramDTO() = ProgramDTO(
    id = id,
    name = name,
    description = description,
    startTime = startTime?.let { LocalTime.parse(it) },
    endTime = endTime?.let { LocalTime.parse(it) },
    location = location,
    suitability = suitabilities.toSuitabilityDTO(),
    eventType = EventType.Program,
    isOnline = false,
    startDate = LocalDate.parse(startDate),
    endDate = LocalDate.parse(endDate),
    frequency = FrequencyType.valueOf(frequency)
)
