package com.breastcancer.breastcancercare.models

import androidx.room.PrimaryKey
import com.breastcancer.breastcancercare.database.local.entity.EventEntity
import com.breastcancer.breastcancercare.database.local.types.EventType
import com.breastcancer.breastcancercare.models.interfaces.ProgramEventDTO
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class EventDTO(
    override val id: Long,
    override val name: String,
    override val description: String,
    override val startTime: LocalTime? = null,
    override val endTime: LocalTime? = null,
    override val location: String? = null,
    override val eventType: EventType = EventType.Event,
    override val isOnline: Boolean = (location == null),
    val date: LocalDate,
    val isFeatured: Boolean = false
): ProgramEventDTO


fun EventEntity.toEventDTO() = EventDTO(
    id = id,
    name = name,
    description = description,
    date = LocalDate.parse(date),
    startTime = startTime?.let { LocalTime.parse(it) },
    endTime = endTime?.let { LocalTime.parse(it) },
    location = location,
    isFeatured = isFeatured
)