package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.BlogDAO
import com.breastcancer.breastcancercare.database.local.entity.BlogEntity
import com.breastcancer.breastcancercare.database.local.entity.CategoryEntity
import com.breastcancer.breastcancercare.models.CategoryDTO
import com.breastcancer.breastcancercare.models.toDTO
import kotlinx.coroutines.flow.map

open class BlogRepository(private val blogDAO: BlogDAO) {
    suspend fun insertAllBlogs(blogs: List<BlogEntity>) = blogDAO.insertAllBlogs(blogs)

    suspend fun insertAllCategories(categories: List<CategoryEntity>) =
        blogDAO.insertAllCategories(categories)

    fun getAllBlogs() = blogDAO.getAllBlogs().map { flow -> flow.map { it.toDTO() } }

    fun getAllCategories() = blogDAO.getAllCategories().map { flow -> flow.map { it.toDTO() } }

    fun getRecommendedBlogs() = blogDAO.getRecommendedBlogs().map { flow -> flow.map { it.toDTO() } }

    suspend fun getBlogById(slug: String) = blogDAO.getBlogBySlug(slug).toDTO()

    fun getBlogByCategories(categories: List<CategoryDTO>) =
        blogDAO.getBlogsBasedOnCategories(categories.map { it.key })
            .map { flow -> flow.map { it.toDTO() } }

    fun getBlogByTags(tags: List<String>) =
        blogDAO.getBlogsBasedOnTags(tags)
            .map { flow -> flow.map { it.toDTO() } }
}