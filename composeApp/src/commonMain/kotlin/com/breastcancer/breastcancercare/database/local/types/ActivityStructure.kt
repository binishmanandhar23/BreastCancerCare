package com.breastcancer.breastcancercare.database.local.types

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

    val frequency: String

    val location: String?
    val onlineLink: String?
}