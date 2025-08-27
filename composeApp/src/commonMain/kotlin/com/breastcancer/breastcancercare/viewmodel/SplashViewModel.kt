package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.repo.OnboardingRepository
import com.breastcancer.breastcancercare.states.SplashUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(val onboardingRepository: OnboardingRepository) : ViewModel() {
    private var _splashUIState = MutableStateFlow<SplashUIState>(SplashUIState.Initial)
    val splashUIState = _splashUIState.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            delay(500L)
            if (onboardingRepository.isLoggedIn())
                onboardingRepository.getLoggedInUser().collect { user ->
                    _splashUIState.update {
                        if (user != null)
                            SplashUIState.LoggedIn
                        else
                            SplashUIState.NotLoggedIn
                    }
                }
            else
                _splashUIState.update {
                    SplashUIState.NotLoggedIn
                }

        }
    }
}