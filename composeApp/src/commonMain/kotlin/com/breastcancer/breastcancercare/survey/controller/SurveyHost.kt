package com.breastcancer.breastcancercare.survey.controller// commonMain

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.breastcancer.breastcancercare.survey.model.Answer
import com.breastcancer.breastcancercare.survey.model.IntAnswer
import com.breastcancer.breastcancercare.survey.model.IntScaleQuestion
import com.breastcancer.breastcancercare.survey.model.Question
import com.breastcancer.breastcancercare.survey.model.Section
import com.breastcancer.breastcancercare.survey.model.SingleChoiceAnswer
import com.breastcancer.breastcancercare.survey.model.SingleChoiceQuestion
import com.breastcancer.breastcancercare.survey.model.Survey
import com.breastcancer.breastcancercare.survey.model.TextAnswer
import com.breastcancer.breastcancercare.survey.model.TextQuestion
import com.breastcancer.breastcancercare.survey.model.getSurveyKeyboardType
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingMedium
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingSmall
import kotlin.math.roundToInt

@Composable
fun SurveyHost(
    survey: Survey,
    modifier: Modifier = Modifier,
    onSubmit: (Map<String, Answer>) -> Unit
) {
    // Basic state – keep in-memory (swap to ViewModel if you prefer)
    var sectionIndex by remember { mutableStateOf(0) }
    val answers = remember { mutableStateMapOf<String, Answer>() }

    fun validateSection(sec: Section): Boolean =
        sec.questions.all { q ->
            !q.required || answers[q.id] != null && when (val a = answers[q.id]) {
                is SingleChoiceAnswer -> !(q is SingleChoiceQuestion && q.allowOther &&
                        a.optionId == "other" && a.otherText.isNullOrBlank())

                is TextAnswer -> a.text.isNotBlank()
                is IntAnswer -> true
                null -> false
            }
        }

    val section by remember(sectionIndex) { derivedStateOf { survey.sections[sectionIndex] } }

    LazyColumn(modifier.padding(16.dp).fillMaxSize()) {
        // Header / progress
        item {
            Text(survey.title, style = MaterialTheme.typography.titleLarge)
        }
        item {
            Spacer(Modifier.height(8.dp))
        }
        item {
            LinearProgressIndicator(
                progress = { (sectionIndex + 1f) / survey.sections.size },
                modifier = Modifier.fillMaxWidth(),
                color = ProgressIndicatorDefaults.linearColor,
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
        }
        item {
            AnimatedContent(section, transitionSpec = {
                (slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                ) + fadeIn(tween(180)))
                    .togetherWith(
                        slideOutHorizontally(
                            animationSpec = tween(300)
                        ) + fadeOut(tween(180))
                    )
                    .using(SizeTransform(clip = true)) // avoid edge bleed
            }) { section ->
                Column(verticalArrangement = Arrangement.spacedBy(DefaultVerticalPaddingSmall)) {
                    section.title?.let {
                        Spacer(Modifier.height(12.dp))
                        Text(it, style = MaterialTheme.typography.titleMedium)
                    }
                    // Questions
                    section.questions.forEach { q ->
                        QuestionRenderer(
                            question = q,
                            answer = answers[q.id],
                            onAnswer = { answers[q.id] = it }
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
        // Nav buttons
        item {
            val enabled by remember(section) { derivedStateOf { validateSection(section) } }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(
                    enabled = sectionIndex > 0,
                    onClick = { sectionIndex-- }
                ) { Text("Back") }

                val last = sectionIndex == survey.sections.lastIndex
                Button(
                    enabled = enabled,
                    onClick = {
                        if (last) onSubmit(answers.toMap()) else sectionIndex++
                    }
                ) { Text(if (last) "Submit" else "Next") }
            }
        }
    }
}

@Composable
private fun QuestionRenderer(
    question: Question,
    answer: Answer?,
    onAnswer: (Answer) -> Unit
) {
    when (question) {
        is SingleChoiceQuestion -> SingleChoiceQuestionView(
            question,
            answer as? SingleChoiceAnswer,
            onAnswer
        )

        is TextQuestion -> TextQuestionView(question, answer as? TextAnswer, onAnswer)
        is IntScaleQuestion -> IntScaleQuestionView(question, answer as? IntAnswer, onAnswer)
    }
}

@Composable
private fun SingleChoiceQuestionView(
    q: SingleChoiceQuestion,
    a: SingleChoiceAnswer?,
    onAnswer: (Answer) -> Unit
) {
    Text(q.prompt, style = MaterialTheme.typography.titleSmall)
    Spacer(Modifier.height(8.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        q.options.forEach { opt ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = a?.optionId == opt.id,
                    onClick = { onAnswer(SingleChoiceAnswer(opt.id, a?.otherText)) }
                )
                Text(opt.label, modifier = Modifier.padding(start = 8.dp))
            }
        }
        if (q.allowOther) {
            val selectedOther = a?.optionId == "other"
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedOther,
                    onClick = { onAnswer(SingleChoiceAnswer("other", a?.otherText)) }
                )
                Text("Other (specify)", modifier = Modifier.padding(start = 8.dp))
            }
            if (selectedOther) {
                var text by remember { mutableStateOf(a.otherText.orEmpty()) }
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        onAnswer(SingleChoiceAnswer("other", it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
private fun TextQuestionView(
    q: TextQuestion,
    a: TextAnswer?,
    onAnswer: (Answer) -> Unit
) {
    Text(q.prompt, style = MaterialTheme.typography.titleSmall)
    Spacer(Modifier.height(8.dp))
    var text by remember { mutableStateOf(a?.text.orEmpty()) }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onAnswer(TextAnswer(it))
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = !q.multiline,
        maxLines = if (q.multiline) 6 else 1,
        keyboardOptions = getSurveyKeyboardType(q.keyboardType)
    )
}

@Composable
private fun IntScaleQuestionView(
    q: IntScaleQuestion,
    a: IntAnswer?,
    onAnswer: (Answer) -> Unit
) {
    Text(q.prompt, style = MaterialTheme.typography.titleSmall)
    Spacer(Modifier.height(8.dp))
    val min = q.startRange
    val max = q.endRange
    var value by remember { mutableStateOf((a?.value ?: min).toFloat()) }

    Column {
        Slider(
            value = value,
            onValueChange = {
                value = it
                onAnswer(IntAnswer(it.roundToInt().coerceIn(min, max)))
            },
            valueRange = min.toFloat()..max.toFloat(),
            steps = (max - min - 1).coerceAtLeast(0)
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(q.leftHint ?: min.toString(), style = MaterialTheme.typography.labelSmall)
            Text(q.rightHint ?: max.toString(), style = MaterialTheme.typography.labelSmall)
        }
    }
}
