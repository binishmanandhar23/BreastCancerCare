package com.breastcancer.breastcancercare.viewmodel

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.breast_cancer_care_wa
import com.breastcancer.breastcancercare.models.TutorialDTO
import com.breastcancer.breastcancercare.repo.TutorialRepository
import com.breastcancer.breastcancercare.states.TutorialUIState
import com.breastcancer.breastcancercare.tutorial_activities_phone
import com.breastcancer.breastcancercare.tutorial_activities_tablet
import com.breastcancer.breastcancercare.tutorial_dashboard_phone
import com.breastcancer.breastcancercare.tutorial_dashboard_tablet
import com.breastcancer.breastcancercare.tutorial_get_started
import com.breastcancer.breastcancercare.tutorial_info_phone
import com.breastcancer.breastcancercare.tutorial_info_tablet
import com.breastcancer.breastcancercare.tutorial_your_journey_phone
import com.breastcancer.breastcancercare.tutorial_your_journey_tablet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TutorialViewModel(val tutorialRepository: TutorialRepository) : ViewModel() {
    private var _tutorialUIState =
        MutableStateFlow<TutorialUIState<List<TutorialDTO>>>(TutorialUIState.Initial())
    val tutorialUIState = _tutorialUIState.asStateFlow()

    init {
        populateTutorials()
    }


    private fun populateTutorials() = viewModelScope.launch {
        _tutorialUIState.update {
            TutorialUIState.Success(
                data = listOf(
                    TutorialDTO(
                        page = 1,
                        title = "Welcome to the BreastCancerCare App",
                        description = "Your personal care app.",
                        borderWidth = 0.dp,
                        phoneImage = Res.drawable.breast_cancer_care_wa
                    ),
                    TutorialDTO(
                        page = 2,
                        title = "Your Journey",
                        description = "Choose your journey based on your condition and have the app personalize the contents accordingly.\nYou also have the ability to switch journey at anytime through the Settings page.",
                        phoneImage = Res.drawable.tutorial_your_journey_phone,
                        tabletImage = Res.drawable.tutorial_your_journey_tablet,
                    ),
                    TutorialDTO(
                        page = 3,
                        title = "Dashboard",
                        description = "Find your suggested activities and recommended blogs in the dashboard.",
                        phoneImage = Res.drawable.tutorial_dashboard_phone,
                        tabletImage = Res.drawable.tutorial_dashboard_tablet,
                    ),
                    TutorialDTO(
                        page = 4,
                        title = "Activities",
                        description = "Get access to an in-app calendar where you can view upcoming activities, along with managing your registered activities.",
                        phoneImage = Res.drawable.tutorial_activities_phone,
                        tabletImage = Res.drawable.tutorial_activities_tablet,
                    ),
                    TutorialDTO(
                        page = 5,
                        title = "Info",
                        description = "Info page is where you can find answers to all of the Frequently Asked Questions",
                        phoneImage = Res.drawable.tutorial_info_phone,
                        tabletImage = Res.drawable.tutorial_info_tablet,
                    ),
                    TutorialDTO(
                        page = 6,
                        title = "You're all set",
                        description = "Press Done to get started",
                        borderWidth = 0.dp,
                        phoneImage = Res.drawable.tutorial_get_started
                    )
                )
            )
        }
    }

    suspend fun updateTutorialViewedById(userId: Long, tutorialViewed: Boolean) =
            tutorialRepository.updateTutorialViewedById(
                userId = userId,
                tutorialViewed = tutorialViewed
            )
}