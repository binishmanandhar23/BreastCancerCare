package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ActivityHistoryWithActivityEntity(
    @Embedded val activityHistory: ActivityHistoryEntity,
    @Relation(
        parentColumn = "activityId",
        entityColumn = "id"
    ) val activity: ActivityEntity?
)