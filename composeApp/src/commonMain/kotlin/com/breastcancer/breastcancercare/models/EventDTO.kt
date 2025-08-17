package com.breastcancer.breastcancercare.models

import androidx.room.PrimaryKey
import com.breastcancer.breastcancercare.database.local.entity.EventEntity
import com.breastcancer.breastcancercare.database.local.types.EventType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class EventDTO(
    val id: Long,
    val name: String,
    val description: String,
    val date: LocalDate,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val location: String? = null,
    val isFeatured: Boolean = false,
    val eventType: EventType = EventType.Event,
    val isOnline: Boolean = (location == null)
)


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