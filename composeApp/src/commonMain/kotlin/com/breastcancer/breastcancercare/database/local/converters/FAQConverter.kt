package com.breastcancer.breastcancercare.database.local.converters

import androidx.room.TypeConverter
import com.breastcancer.breastcancercare.database.local.entity.SuitabilityEntity
import kotlinx.serialization.json.Json

object FAQConverter {
    val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun suitabilityEntityToString(entity: SuitabilityEntity?): String? =
        entity?.let { json.encodeToString(it) }

    @TypeConverter
    fun stringToSuitabilityEntity(string: String?): SuitabilityEntity? =
        string?.let { json.decodeFromString(it) }

    @TypeConverter
    fun listOfSuitabilityEntityToString(entity: List<SuitabilityEntity>): String =
        json.encodeToString(entity)

    @TypeConverter
    fun stringToListOfSuitabilityEntity(string: String?): List<SuitabilityEntity> =
        string?.let { json.decodeFromString(it) } ?: listOf()
}