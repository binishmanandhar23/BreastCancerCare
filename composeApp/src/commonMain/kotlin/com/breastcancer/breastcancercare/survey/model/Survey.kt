package com.breastcancer.breastcancercare.survey.model// commonMain

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.serialization.Serializable


@Serializable
data class Surveys(val preSurvey: PreSurvey? = null, val postSurvey: PostSurvey? = null)


@Serializable
data class PreSurvey(
    override val id: String,
    override val mandatory: Boolean = false,
    override val title: String,
    override val sections: List<Section>
) : Survey

@Serializable
data class PreSurveyAnswer(
    override val questionId: String,
    override val answer: Answer, override val answerType: String
) : SurveyAnswer

@Serializable
data class PostSurveyAnswer(
    override val questionId: String,
    override val answer: Answer, override val answerType: String
) : SurveyAnswer

@Serializable
data class PostSurvey(
    override val id: String,
    override val mandatory: Boolean = false,
    override val title: String,
    override val sections: List<Section>
) : Survey

@Stable
@Serializable
sealed interface Survey {
    val id: String
    val mandatory: Boolean
    val title: String
    val sections: List<Section>
}

@Serializable
sealed interface SurveyAnswer {
    val questionId: String
    val answer: Answer
    val answerType: String //AnswerType
}

@Stable
@Serializable
data class Section(
    val id: String,
    val title: String? = null,
    val questions: List<Question>
)

@Serializable
sealed interface Question {
    val id: String
    val prompt: String
    val required: Boolean
}

@Serializable
data class Option(val id: String, val label: String)

@Serializable
data class SingleChoiceQuestion(
    override val id: String,
    override val prompt: String,
    val options: List<Option>,
    val allowOther: Boolean = false,
    override val required: Boolean = true
) : Question

@Serializable
data class TextQuestion(
    override val id: String,
    override val prompt: String,
    val multiline: Boolean = false,
    val keyboardType: String = KeyboardTypeSurvey.Default.type,
    override val required: Boolean = true
) : Question

@Serializable
data class IntScaleQuestion(
    override val id: String,
    override val prompt: String,
    val startRange: Int,
    val endRange: Int,
    val leftHint: String? = null,
    val rightHint: String? = null,
    override val required: Boolean = true
) : Question

/* ---------- Answers ---------- */

@Serializable
sealed interface Answer {
    val answerType: String //AnswerType
}

@Serializable
data class SingleChoiceAnswer(
    val optionId: String, val otherText: String? = null,
    override val answerType: String = AnswerType.SingleChoice.type
) : Answer

@Serializable
data class TextAnswer(val text: String, override val answerType: String = AnswerType.Text.type) :
    Answer

@Serializable
data class IntAnswer(val value: Int, override val answerType: String = AnswerType.IntScale.type) :
    Answer

enum class AnswerType(val type: String) {
    SingleChoice("single_choice"),
    Text("text"),
    IntScale("int_scale")
}

enum class KeyboardTypeSurvey(val type: String) {
    Number(type = "number"),
    Default(type = "default")
}

fun getSurveyKeyboardType(type: String): KeyboardOptions {
    return when (type) {
        KeyboardTypeSurvey.Number.type -> KeyboardOptions(keyboardType = KeyboardType.Number)
        else -> KeyboardOptions.Default
    }
}

fun Map<String, Answer>?.toPreSurveyAnswers(): List<PreSurveyAnswer> = this?.map { (k, v) ->
    PreSurveyAnswer(
        questionId = k,
        answer = v,
        answerType = v.answerType
    )
}?: emptyList()

fun Map<String, Answer>.toPostSurveyAnswers(): List<PostSurveyAnswer> = this?.map { (k, v) ->
    PostSurveyAnswer(
        questionId = k,
        answer = v,
        answerType = v.answerType
    )
}?: emptyList()