package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.ActivityEntity
import com.breastcancer.breastcancercare.database.local.types.ActivityType
import com.breastcancer.breastcancercare.database.local.types.ActivityUtils
import com.breastcancer.breastcancercare.database.local.types.FrequencyType
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ActivityDTO(
    val id: Long,
    val title: String,
    val description: String,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val location: String? = null,
    val category: UserCategory,
    val isOnline: Boolean = (location == null),
    val image: String? = null,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val onlineLink: String? = null,
    val audience: String? = null,
    val frequency: FrequencyType,
    val activityType: ActivityType
)


fun ActivityEntity.toEventDTO() = ActivityDTO(
    id = id,
    title = title,
    description = description,
    image = image,
    startDate = LocalDate.parse(startDate),
    endDate = if(endDate != null) LocalDate.parse(endDate) else null,
    startTime = startTime?.let { LocalTime.parse(it) },
    endTime = endTime?.let { LocalTime.parse(it) },
    location = location,
    category = UserCategory.fromCategory(category),
    isOnline = isOnline,
    onlineLink = onlineLink,
    audience = audience,
    frequency = FrequencyType.valueOf(frequency),
    activityType = ActivityUtils.fromType(category = UserCategory.fromCategory(category), type = activityType)
)