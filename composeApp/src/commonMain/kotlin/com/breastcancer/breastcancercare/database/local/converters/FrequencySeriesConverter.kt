package com.breastcancer.breastcancercare.database.local.converters

import androidx.room.TypeConverter
import com.breastcancer.breastcancercare.models.FrequencySeries
import kotlinx.serialization.json.Json

object FrequencySeriesConverter {
    val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun frequencySeriesToString(frequencySeries: FrequencySeries?): String? =
        frequencySeries?.let { json.encodeToString(it) }

    @TypeConverter
    fun stringToFrequencySeries(string: String?): FrequencySeries? =
        string?.let { json.decodeFromString(it) }
}