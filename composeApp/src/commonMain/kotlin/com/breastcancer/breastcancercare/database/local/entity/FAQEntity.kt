package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breastcancer.breastcancercare.database.local.types.UserCategory

@Entity
data class FAQEntity(
    @PrimaryKey val id: Long = 0L,
    val suitabilities: List<SuitabilityEntity>,
    val userCategory: String, //UserCategory
    val question: String,
    val answer: String
)