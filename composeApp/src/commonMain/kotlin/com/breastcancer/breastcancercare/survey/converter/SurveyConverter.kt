package com.breastcancer.breastcancercare.survey.converter

import androidx.room.TypeConverter
import com.breastcancer.breastcancercare.survey.model.Survey
import com.breastcancer.breastcancercare.survey.model.Surveys
import kotlinx.serialization.json.Json

object SurveyConverter {
    val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun surveysToString(surveys: Surveys?): String? =
        surveys?.let { json.encodeToString(it) }

    @TypeConverter
    fun stringToSurveys(string: String?): Surveys? =
        string?.let { json.decodeFromString(it) }
}