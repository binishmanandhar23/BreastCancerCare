package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breastcancer.breastcancercare.survey.model.PostSurveyAnswer
import com.breastcancer.breastcancercare.survey.model.PreSurveyAnswer

@Entity
data class ActivityHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val activityId: Long,
    val registeredForDate: String,
    val preSurveyAnswers: List<PreSurveyAnswer> = emptyList(),
    val postSurveyAnswers: List<PostSurveyAnswer> = emptyList()
)
