package com.breastcancer.breastcancercare.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breastcancer.breastcancercare.database.local.entity.BlogEntity
import com.breastcancer.breastcancercare.database.local.entity.BlogCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBlogs(blogEntities: List<BlogEntity>)

    @Query("SELECT * FROM blogentity")
    fun getAllBlogs(): Flow<List<BlogEntity>>

    @Query("SELECT * FROM blogentity WHERE slug = :slug")
    suspend fun getBlogBySlug(slug: String): BlogEntity

    @Query("SELECT * FROM blogentity ORDER BY RANDOM() LIMIT 5")
    fun getRecommendedBlogs(): Flow<List<BlogEntity>>

    /*@Query("SELECT * FROM blogentity WHERE categories IN (:categories)")
    fun getBlogsBasedOnCategories(categories: List<String>): Flow<List<BlogEntity>>*/

    @Query("SELECT * FROM blogentity WHERE tags IN (:tags)")
    fun getBlogsBasedOnTags(tags: List<String>): Flow<List<BlogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(categories: List<BlogCategoryEntity>)

    @Query("SELECT * FROM blogcategoryentity")
    fun getAllCategories(): Flow<List<BlogCategoryEntity>>

}