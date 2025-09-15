package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.ActivityEntity
import com.breastcancer.breastcancercare.database.local.types.ActivityType
import com.breastcancer.breastcancercare.database.local.types.ActivityUtils
import com.breastcancer.breastcancercare.database.local.types.FrequencyType
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.utils.getDatesFromActivity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ActivityDTO(
    val id: Long,
    val title: String,
    val description: String,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val location: Location? = null,
    val category: UserCategory,
    val isOnline: Boolean = (location == null),
    val image: String? = null,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val onlineLink: String? = null,
    val audience: String? = null,
    val frequency: FrequencyType,
    val frequencySeries: FrequencySeries? = null,
    val activityType: ActivityType,
    val dates: List<LocalDate> = emptyList()
)


fun ActivityEntity.toActivityDTO(): ActivityDTO {
    val frequency = FrequencyType.valueOf(frequency)
    val startDate = LocalDate.parse(startDate)
    val endDate = if (endDate != null) LocalDate.parse(endDate) else null
    return ActivityDTO(
        id = id,
        title = title,
        description = description,
        image = image,
        startDate = startDate,
        endDate = endDate,
        startTime = startTime?.let { LocalTime.parse(it) },
        endTime = endTime?.let { LocalTime.parse(it) },
        location = location,
        category = UserCategory.fromCategory(category),
        isOnline = isOnline,
        onlineLink = onlineLink,
        audience = audience,
        frequency = frequency,
        frequencySeries = frequencySeries,
        activityType = ActivityUtils.fromType(
            category = UserCategory.fromCategory(category),
            type = activityType
        ),
        dates = getDatesFromActivity(
            frequencyType = frequency,
            startDate = startDate,
            endDate = endDate,
            frequencySeries = frequencySeries
        )
    )
}