package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.ActivityHistoryEntity
import com.breastcancer.breastcancercare.database.local.entity.ActivityHistoryWithActivityEntity
import com.breastcancer.breastcancercare.survey.model.PostSurveyAnswer
import com.breastcancer.breastcancercare.survey.model.PreSurveyAnswer
import kotlinx.datetime.LocalDate


data class ActivityHistoryDTO(
    val id: Long? = null,
    val userId: Long,
    val activityId: Long,
    val activity: ActivityDTO? = null,
    val registeredForDate: LocalDate,
    val preSurveyAnswers: List<PreSurveyAnswer> = emptyList(),
    val postSurveyAnswers: List<PostSurveyAnswer> = emptyList()
)

fun ActivityHistoryWithActivityEntity.toDTO() = ActivityHistoryDTO(
    id = activityHistory.id,
    userId = activityHistory.userId,
    activityId = activityHistory.activityId,
    activity = activity?.toActivityDTO(),
    registeredForDate = LocalDate.parse(activityHistory.registeredForDate),
    preSurveyAnswers = activityHistory.preSurveyAnswers,
    postSurveyAnswers = activityHistory.postSurveyAnswers
)

fun ActivityHistoryEntity.toDTO() =
    ActivityHistoryDTO(
        id = id,
        userId = userId,
        activityId = activityId,
        registeredForDate = LocalDate.parse(registeredForDate),
        preSurveyAnswers = preSurveyAnswers,
        postSurveyAnswers = postSurveyAnswers
    )


fun ActivityHistoryDTO.toEntity() = ActivityHistoryEntity(
    activityId = activityId,
    userId = userId,
    registeredForDate = registeredForDate.toString(),
    preSurveyAnswers = preSurveyAnswers,
    postSurveyAnswers = postSurveyAnswers
).let {
    if (id != null) it.copy(id = id) else it
}