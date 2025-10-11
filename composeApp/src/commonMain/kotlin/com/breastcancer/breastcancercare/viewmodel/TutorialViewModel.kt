package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.Res
import com.breastcancer.breastcancercare.breast_cancer_care_wa
import com.breastcancer.breastcancercare.models.TutorialDTO
import com.breastcancer.breastcancercare.repo.TutorialRepository
import com.breastcancer.breastcancercare.states.TutorialUIState
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
                        title = "Welcome to BreastCancerCare App",
                        description = "Your personal care app.",
                        image = Res.drawable.breast_cancer_care_wa
                    ),
                    TutorialDTO(
                        page = 2,
                        title = "Your Journey",
                        description = "Choose your journey based on your condition and have the app personalize the contents accordingly.\nYou also have the ability to switch journey at anytime through the Settings page.",
                        image = Res.drawable.breast_cancer_care_wa
                    ),
                    TutorialDTO(
                        page = 3,
                        title = "Dashboard",
                        description = "Find your suggested activities and recommended blogs in the dashboard.",
                        image = Res.drawable.breast_cancer_care_wa
                    ),
                    TutorialDTO(
                        page = 4,
                        title = "Activities",
                        description = "Get access to an in-app calendar where you can view upcoming activities, along with managing your registered activities.",
                        image = Res.drawable.breast_cancer_care_wa
                    ),
                    TutorialDTO(
                        page = 5,
                        title = "Info",
                        description = "Info page is where you can find answers to all of the Frequently Asked Questions",
                        image = Res.drawable.breast_cancer_care_wa
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