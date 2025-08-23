package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import com.breastcancer.breastcancercare.repo.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OnboardingViewModel(val onboardingRepository: OnboardingRepository): ViewModel() {
    private var _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private var _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun setEmail(email: String) = _email.update { email }
    fun setPassword(password: String) = _password.update { password }
}