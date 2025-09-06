package com.breastcancer.breastcancercare.database.local.converters

import androidx.room.TypeConverter
import com.breastcancer.breastcancercare.database.local.entity.CategoryEntity
import kotlinx.serialization.json.Json


object BlogConverter {
    val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun suitabilityEntityToString(entity: CategoryEntity?): String? =
        entity?.let { json.encodeToString(it) }

    @TypeConverter
    fun stringToCategoryEntity(string: String?): CategoryEntity? =
        string?.let { json.decodeFromString(it) }

    @TypeConverter
    fun listOfCategoryEntityToString(entity: List<CategoryEntity>): String =
        json.encodeToString(entity)

    @TypeConverter
    fun stringToListOfCategoryEntity(string: String?): List<CategoryEntity> =
        string?.let { json.decodeFromString(it) } ?: listOf()
}