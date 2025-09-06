package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.BlogEntity

data class BlogDTO(
    val slug: String,
    val title: String,
    val body: String,
    val image: String,
    val categories: List<CategoryDTO>,
    val tags: List<String>
)

fun BlogEntity.toDTO() = BlogDTO(
    slug = slug,
    title = title,
    body = body,
    image = image,
    categories = categories.map { it.toDTO() },
    tags = tags
)
