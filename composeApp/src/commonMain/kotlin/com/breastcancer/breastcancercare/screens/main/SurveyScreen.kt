package com.breastcancer.breastcancercare.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.breastcancer.breastcancercare.states.ActivityUIState
import com.breastcancer.breastcancercare.survey.controller.SurveyHost
import com.breastcancer.breastcancercare.viewmodel.ActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SurveyScreen(activityViewModel: ActivityViewModel, id: Long){
    val activityUIState by activityViewModel.activityUIDetailState.collectAsStateWithLifecycle()
    LaunchedEffect(id) {
        withContext(Dispatchers.Main) {
            activityViewModel.getActivityById(id = id)
        }
    }
    AnimatedContent(modifier = Modifier.fillMaxSize(), targetState = activityUIState) { state ->
        when (state) {
            is ActivityUIState.Success -> {
                val activity by remember { derivedStateOf { state.data } }
                activity?.surveys?.preSurvey?.let {
                    SurveyHost(survey = it){

                    }
                }
            }
            else -> Unit
        }
    }
}