package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.ActivityScheduleEntity
import com.breastcancer.breastcancercare.database.local.entity.ActivityScheduleWithActivityEntity
import com.breastcancer.breastcancercare.survey.model.PostSurveyAnswer
import com.breastcancer.breastcancercare.survey.model.PreSurveyAnswer
import kotlinx.datetime.LocalDate


data class ActivityScheduleDTO(
    val id: Long? = null,
    val userId: Long,
    val activityId: Long,
    val activity: ActivityDTO? = null,
    val registeredForDate: LocalDate,
    val preSurveyAnswers: List<PreSurveyAnswer> = emptyList(),
    val postSurveyAnswers: List<PostSurveyAnswer> = emptyList()
)

fun ActivityScheduleWithActivityEntity.toDTO() = ActivityScheduleDTO(
    id = activityHistory.id,
    userId = activityHistory.userId,
    activityId = activityHistory.activityId,
    activity = activity?.toActivityDTO(),
    registeredForDate = LocalDate.parse(activityHistory.registeredForDate),
    preSurveyAnswers = activityHistory.preSurveyAnswers,
    postSurveyAnswers = activityHistory.postSurveyAnswers
)

fun ActivityScheduleEntity.toDTO() =
    ActivityScheduleDTO(
        id = id,
        userId = userId,
        activityId = activityId,
        registeredForDate = LocalDate.parse(registeredForDate),
        preSurveyAnswers = preSurveyAnswers,
        postSurveyAnswers = postSurveyAnswers
    )


fun ActivityScheduleDTO.toEntity() = ActivityScheduleEntity(
    activityId = activityId,
    userId = userId,
    registeredForDate = registeredForDate.toString(),
    preSurveyAnswers = preSurveyAnswers,
    postSurveyAnswers = postSurveyAnswers
).let {
    if (id != null) it.copy(id = id) else it
}