package com.breastcancer.breastcancercare.database.local.types

import com.breastcancer.breastcancercare.models.FrequencySeries
import com.breastcancer.breastcancercare.models.Location
import com.breastcancer.breastcancercare.survey.model.Surveys

interface ActivityStructure {
    val id: Long

    val image: String?
    val title: String
    val category: String // ⬅️ UserCategory
    val activityType: String // ⬅️ ActivityType
    val description: String
    val audience: String // Basically who it's for (will be string)

    val startDate: String
    val endDate: String?
    val startTime: String?
    val endTime: String?
    val isOnline: Boolean

    val location: Location?

    val onlineLink: String?
    val frequency: String

    val frequencySeries: FrequencySeries?

    val surveys: Surveys?
}
