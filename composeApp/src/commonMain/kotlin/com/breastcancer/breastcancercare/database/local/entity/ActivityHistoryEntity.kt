package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.breastcancer.breastcancercare.survey.model.PostSurveyAnswer
import com.breastcancer.breastcancercare.survey.model.PreSurveyAnswer

@Entity(
    foreignKeys = [ForeignKey(
        entity = ActivityEntity::class,
        parentColumns = ["id"],
        childColumns = ["activityId"]
    )],
    indices = [Index("activityId")]
)
data class ActivityHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val userId: Long,
    val activityId: Long,
    val registeredForDate: String,
    val preSurveyAnswers: List<PreSurveyAnswer> = emptyList(),
    val postSurveyAnswers: List<PostSurveyAnswer> = emptyList()
)
