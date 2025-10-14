package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ActivityScheduleWithActivityEntity(
    @Embedded val activityHistory: ActivityScheduleEntity,
    @Relation(
        parentColumn = "activityId",
        entityColumn = "id"
    ) val activity: ActivityEntity?
)