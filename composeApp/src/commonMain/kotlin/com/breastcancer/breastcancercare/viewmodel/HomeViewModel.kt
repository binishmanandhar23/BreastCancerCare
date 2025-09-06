package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.models.BlogDTO
import com.breastcancer.breastcancercare.repo.HomeRepository
import com.breastcancer.breastcancercare.states.HomeUIState
import com.breastcancer.breastcancercare.utils.getHomeGreetingText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(val homeRepository: HomeRepository) : ViewModel() {
    private val _homeGreeting = MutableStateFlow("")
    val homeGreeting = _homeGreeting.asStateFlow()

    private var _recommendedBlogUIState =
        MutableStateFlow<HomeUIState<List<BlogDTO>>>(HomeUIState.Initial())
    val recommendedBlogUIState = _recommendedBlogUIState.asStateFlow()

    init {
        getHomeGreeting()
        getRecommendedBlogs()
    }

    fun getHomeGreeting() = viewModelScope.launch(Dispatchers.IO) {
        homeRepository.getLoggedInUser().collect { user ->
            _homeGreeting.update {
                getHomeGreetingText(userName = user?.firstName)
            }
        }
    }

    private fun getRecommendedBlogs() = viewModelScope.launch(Dispatchers.IO) {
        delay(2000L)
        _recommendedBlogUIState.update { HomeUIState.Loading() }
        delay(2500L)
        homeRepository.getRecommendedBlogs().collect { recommendedBlogs ->
            _recommendedBlogUIState.update { _ ->
                if (recommendedBlogs.isEmpty()) HomeUIState.Empty() else HomeUIState.Success(
                    data = recommendedBlogs
                )
            }
        }
    }

}