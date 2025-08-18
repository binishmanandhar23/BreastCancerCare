package com.breastcancer.breastcancercare.models.interfaces

import com.breastcancer.breastcancercare.database.local.entity.ProgramEntity
import com.breastcancer.breastcancercare.database.local.types.EventType
import com.breastcancer.breastcancercare.database.local.types.Suitability
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ProgramDTO(
    override val id: Long,
    override val name: String,
    override val description: String,
    override val date: LocalDate,
    override val startTime: LocalTime?,
    override val endTime: LocalTime?,
    override val location: String?,
    override val eventType: EventType,
    override val isOnline: Boolean,
    val suitability: Suitability
) : ProgramEventDTO

fun ProgramEntity.toProgramDTO() = ProgramDTO(
    id = id,
    name = name,
    description = description,
    date = LocalDate.parse(date),
    startTime = startTime?.let { LocalTime.parse(it) },
    endTime = endTime?.let { LocalTime.parse(it) },
    location = location,
    suitability = Suitability.valueOf(suitability),
    eventType = EventType.Program,
    isOnline = false
)
