package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.BlogDAO
import com.breastcancer.breastcancercare.database.local.entity.BlogEntity
import com.breastcancer.breastcancercare.database.local.entity.BlogCategoryEntity
import com.breastcancer.breastcancercare.models.BlogCategoryDTO
import com.breastcancer.breastcancercare.models.toDTO
import kotlinx.coroutines.flow.map

open class BlogRepository(private val blogDAO: BlogDAO) {
    suspend fun insertAllBlogs(blogs: List<BlogEntity>) = blogDAO.insertAllBlogs(blogs)

    suspend fun insertAllCategories(categories: List<BlogCategoryEntity>) =
        blogDAO.insertAllCategories(categories)

    fun getAllBlogs() = blogDAO.getAllBlogs().map { flow -> flow.map { it.toDTO() } }

    fun getAllCategories() = blogDAO.getAllCategories().map { flow -> flow.map { it.toDTO() } }

    fun getRecommendedBlogs() =
        blogDAO.getRecommendedBlogs().map { flow -> flow.map { it.toDTO() } }

    suspend fun getBlogById(slug: String) = blogDAO.getBlogBySlug(slug).toDTO()

    fun getBlogByCategories(categories: List<BlogCategoryDTO>) =
        blogDAO.getAllBlogs()
            .map { flow ->
                flow.filter { blog ->
                    blog.categories.map { it.key }.any { key -> categories.map { it.key }.contains(key) }
                }.map { it.toDTO() }
            }

    fun getBlogByTags(tags: List<String>) =
        blogDAO.getBlogsBasedOnTags(tags)
            .map { flow -> flow.map { it.toDTO() } }
}