package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breastcancer.breastcancercare.database.local.types.ActivityStructure

@Entity
data class ActivityEntity(
    @PrimaryKey override val id: Long,
    override val title: String,
    override val description: String,
    override val image: String? = null,
    override val startDate: String,
    override val endDate: String?,
    override val startTime: String? = null,
    override val endTime: String? = null,
    override val location: String? = null,
    override val category: String,
    override val isOnline: Boolean,
    override val onlineLink: String?,
    override val audience: String,
    override val activityType: String,
    override val frequency: String
): ActivityStructure
