package com.breastcancer.breastcancercare.database.local.converters

import androidx.room.TypeConverter
import com.breastcancer.breastcancercare.models.Location
import kotlinx.serialization.json.Json

object LocationConverter {
    val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun locationToString(location: Location?): String? =
        location?.let { json.encodeToString(it) }

    @TypeConverter
    fun stringToLocation(string: String?): Location? =
        string?.let { json.decodeFromString(it) }
}