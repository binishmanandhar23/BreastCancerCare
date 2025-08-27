package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class SuitabilityEntity(
    @PrimaryKey val key: String,
    val name: String,
    val description: String?
)

