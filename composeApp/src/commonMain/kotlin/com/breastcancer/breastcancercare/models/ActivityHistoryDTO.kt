package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.ActivityHistoryEntity
import com.breastcancer.breastcancercare.survey.model.PostSurveyAnswer
import com.breastcancer.breastcancercare.survey.model.PreSurveyAnswer
import kotlinx.datetime.LocalDate

data class ActivityHistoryDTO(
    val id: Long? = null,
    val activityId: Long,
    val registeredForDate: LocalDate,
    val preSurveyAnswers: List<PreSurveyAnswer> = emptyList(),
    val postSurveyAnswers: List<PostSurveyAnswer> = emptyList()
)

fun ActivityHistoryEntity.toDTO() =
    ActivityHistoryDTO(
        id = id,
        activityId = activityId,
        registeredForDate = LocalDate.parse(registeredForDate),
        preSurveyAnswers = preSurveyAnswers,
        postSurveyAnswers = postSurveyAnswers
    )


fun ActivityHistoryDTO.toEntity() = ActivityHistoryEntity(
    activityId = activityId,
    registeredForDate = registeredForDate.toString(),
    preSurveyAnswers = preSurveyAnswers,
    postSurveyAnswers = postSurveyAnswers
).let {
    if (id != null) it.copy(id = id) else it
}