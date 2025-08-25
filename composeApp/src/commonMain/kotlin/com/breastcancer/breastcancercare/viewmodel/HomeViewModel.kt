package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.repo.HomeRepository
import com.breastcancer.breastcancercare.utils.getHomeGreetingText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(val homeRepository: HomeRepository): ViewModel() {
    private val _homeGreeting = MutableStateFlow("")
    val homeGreeting = _homeGreeting.asStateFlow()


    init {
        getHomeGreeting()
    }

    fun getHomeGreeting() = viewModelScope.launch(Dispatchers.IO) {
        homeRepository.getLoggedInUser().collect { user ->
            _homeGreeting.update {
                getHomeGreetingText(userName = user?.firstName)
            }
        }
    }
}