package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.CategoryEntity

data class CategoryDTO(val key: String, val name: String)

fun CategoryEntity.toDTO() = CategoryDTO(key = this.key, name = this.name)
