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
): Survey

@Serializable
data class PostSurvey(
    override val id: String,
    override val mandatory: Boolean = false,
    override val title: String,
    override val sections: List<Section>
): Survey

@Stable
@Serializable
sealed interface Survey {
    val id: String
    val mandatory: Boolean
    val title: String
    val sections: List<Section>
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
sealed interface Answer
@Serializable
data class SingleChoiceAnswer(val optionId: String, val otherText: String? = null) : Answer
@Serializable
data class TextAnswer(val text: String) : Answer
@Serializable
data class IntAnswer(val value: Int) : Answer

enum class KeyboardTypeSurvey(val type: String){
    Number(type = "number"),
    Default(type = "default")
}

fun getSurveyKeyboardType(type: String): KeyboardOptions {
    return when (type) {
        KeyboardTypeSurvey.Number.type -> KeyboardOptions(keyboardType = KeyboardType.Number)
        else -> KeyboardOptions.Default
    }
}
