package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FAQEntity(@PrimaryKey val id: Long = 0L, val question: String, val answer: String)