package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breastcancer.breastcancercare.database.local.types.ProgramEvent

@Entity
data class ProgramEntity(
    @PrimaryKey override val id: Long,
    override val name: String,
    override val description: String,
    override val eventType: String,
    override val startTime: String? = null,
    override val endTime: String? = null,
    override val isOnline: Boolean,
    override val location: String?,
    val frequency: String,
    val startDate: String,
    val endDate: String,
    val suitabilities: List<SuitabilityEntity>
): ProgramEvent
