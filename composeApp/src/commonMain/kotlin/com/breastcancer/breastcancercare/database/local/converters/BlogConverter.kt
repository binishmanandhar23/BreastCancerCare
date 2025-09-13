package com.breastcancer.breastcancercare.database.local.converters

import androidx.room.TypeConverter
import com.breastcancer.breastcancercare.database.local.entity.BlogCategoryEntity
import kotlinx.serialization.json.Json


object BlogConverter {
    val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun suitabilityEntityToString(entity: BlogCategoryEntity?): String? =
        entity?.let { json.encodeToString(it) }

    @TypeConverter
    fun stringToCategoryEntity(string: String?): BlogCategoryEntity? =
        string?.let { json.decodeFromString(it) }

    @TypeConverter
    fun listOfCategoryEntityToString(entity: List<BlogCategoryEntity>): String =
        json.encodeToString(entity)

    @TypeConverter
    fun stringToListOfCategoryEntity(string: String?): List<BlogCategoryEntity> =
        string?.let { json.decodeFromString(it) } ?: listOf()
}