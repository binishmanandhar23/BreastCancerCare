package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.BlogCategoryEntity

data class BlogCategoryDTO(val key: String, val name: String)

fun BlogCategoryEntity.toDTO() = BlogCategoryDTO(key = this.key, name = this.name)
