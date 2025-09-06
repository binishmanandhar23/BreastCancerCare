package com.breastcancer.breastcancercare.database.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

object ListConverter {
    val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun listToJson(v: List<String>?): String? = v?.let { json.encodeToString(it) }

    @TypeConverter
    fun jsonToList(s: String?): List<String>? = s?.let { json.decodeFromString<List<String>>(it) }
}