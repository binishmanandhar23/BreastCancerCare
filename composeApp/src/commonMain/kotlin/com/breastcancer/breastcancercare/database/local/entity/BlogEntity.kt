package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BlogEntity(
    @PrimaryKey val slug: String,
    val title: String,
    val body: String,
    val image: String,
    val categories: List<CategoryEntity>,
    val tags:List<String>
)
