package com.breastcancer.breastcancercare.screens.main.survey

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.components.BreastCancerToolbar
import com.breastcancer.breastcancercare.components.loader.LoaderState
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.states.ActivityUIState
import com.breastcancer.breastcancercare.survey.controller.SurveyHost
import com.breastcancer.breastcancercare.survey.model.Answer
import com.breastcancer.breastcancercare.theme.DefaultVerticalPaddingLarge
import com.breastcancer.breastcancercare.viewmodel.ActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SurveyScreen(
    activityViewModel: ActivityViewModel,
    id: Long,
    loaderState: LoaderState,
    onSurveySubmit: (activity: ActivityDTO, Map<String, Answer>) -> Unit,
    onSkipped: (activity: ActivityDTO) -> Unit,
    onBack: () -> Unit
) {
    val activityUIState by activityViewModel.activityUIDetailState.collectAsStateWithLifecycle()
    LaunchedEffect(id) {
        withContext(Dispatchers.Main) {
            activityViewModel.getActivityById(id = id)
        }
    }
    LaunchedEffect(activityUIState){
        when(activityUIState){
            is ActivityUIState.Loading -> loaderState.show()
            is ActivityUIState.Final -> loaderState.hide().also {
                onBack()
            }
            else -> loaderState.hide()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(modifier = Modifier.fillMaxSize(), targetState = activityUIState) { state ->
            when (state) {
                is ActivityUIState.Success -> {
                    val activity by remember { derivedStateOf { state.data } }
                    activity?.surveys?.preSurvey?.let {
                        SurveyHost(
                            modifier = Modifier.padding(top = DefaultVerticalPaddingLarge + DefaultVerticalPaddingLarge),
                            survey = it,
                            onSubmit = { answers ->
                                onSurveySubmit(activity!!, answers)
                            }
                        )
                    }
                }

                else -> Unit
            }
        }
        BreastCancerToolbar(
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
            title = "Survey",
            onEndContent = {
                if (activityUIState is ActivityUIState.Success) {
                    val activity = activityUIState.data
                    activity?.surveys?.let { surveys ->
                        if (surveys.preSurvey?.mandatory == false || surveys.postSurvey?.mandatory == false)
                            TextButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = {
                                onSkipped(activity)
                            }) {
                                Text(
                                    text = "Skip",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    textDecoration = TextDecoration.Underline
                                )
                            }
                    }
                }
            },
            onBack = onBack
        )
    }
}