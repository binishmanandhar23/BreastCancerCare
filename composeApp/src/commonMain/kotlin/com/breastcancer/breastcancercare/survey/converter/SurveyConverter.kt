package com.breastcancer.breastcancercare.survey.converter

import androidx.room.TypeConverter
import com.breastcancer.breastcancercare.survey.model.PostSurveyAnswer
import com.breastcancer.breastcancercare.survey.model.PreSurveyAnswer
import com.breastcancer.breastcancercare.survey.model.Survey
import com.breastcancer.breastcancercare.survey.model.SurveyAnswer
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

    @TypeConverter
    fun surveyAnswerToString(surveys: SurveyAnswer?): String? =
        surveys?.let { json.encodeToString(it) }

    @TypeConverter
    fun stringToSurveyAnswer(string: String?): SurveyAnswer? =
        string?.let { json.decodeFromString(it) }


    @TypeConverter
    fun preSurveyAnswersToString(surveys: List<PreSurveyAnswer>?): String? =
        surveys?.let { json.encodeToString(it) }

    @TypeConverter
    fun stringToPreSurveyAnswers(string: String?): List<PreSurveyAnswer>? =
        string?.let { json.decodeFromString(it) }

    @TypeConverter
    fun postSurveyAnswersToString(surveys: List<PostSurveyAnswer>?): String? =
        surveys?.let { json.encodeToString(it) }

    @TypeConverter
    fun stringToPostSurveyAnswers(string: String?): List<PostSurveyAnswer>? =
        string?.let { json.decodeFromString(it) }

}