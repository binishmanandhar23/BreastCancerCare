package com.breastcancer.breastcancercare.models

data class GuideDTO(
    val id: String,
    val title: String,
    val summary: String,
    val category: String,
    val readTimeMin: Int,
    val updatedAt: Long,
    val sourceUrl: String? = null
)
