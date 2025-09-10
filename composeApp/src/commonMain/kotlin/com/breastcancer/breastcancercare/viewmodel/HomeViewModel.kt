package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.models.BlogDTO
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.repo.HomeRepository
import com.breastcancer.breastcancercare.states.HomeUIState
import com.breastcancer.breastcancercare.utils.getHomeGreetingText
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

class HomeViewModel(val homeRepository: HomeRepository) : ViewModel() {
    private val _homeGreeting = MutableStateFlow("")
    val homeGreeting = _homeGreeting.asStateFlow()

    private var _recommendedBlogsUIState =
        MutableStateFlow<HomeUIState<List<BlogDTO>>>(HomeUIState.Initial())
    val recommendedBlogsUIState = _recommendedBlogsUIState.asStateFlow()

    private var _upcomingEventsUIState =
        MutableStateFlow<HomeUIState<List<ActivityDTO>>>(HomeUIState.Initial())
    val upcomingEventsUIState = _upcomingEventsUIState.asStateFlow()

    init {
        getHomeGreeting()
        getRecommendedBlogs()
        getAllUpcomingEvents()
    }

    fun getHomeGreeting() = viewModelScope.launch(Dispatchers.IO) {
        homeRepository.getLoggedInUser().collect { user ->
            _homeGreeting.update {
                getHomeGreetingText(userName = "${user?.firstName}")
            }
        }
    }

    private fun getRecommendedBlogs() = viewModelScope.launch(Dispatchers.IO) {
        delay(2000L)
        _recommendedBlogsUIState.update { HomeUIState.Loading() }
        delay(2500L)
        homeRepository.getRecommendedBlogs().collect { recommendedBlogs ->
            _recommendedBlogsUIState.update { _ ->
                if (recommendedBlogs.isEmpty()) HomeUIState.Empty() else HomeUIState.Success(
                    data = recommendedBlogs
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun getAllUpcomingEvents() = viewModelScope.launch(Dispatchers.IO) {
        delay(1000L)
        _upcomingEventsUIState.update { HomeUIState.Loading() }
        delay(1500L)
        homeRepository.getAllEvents().collectLatest { events ->
            _upcomingEventsUIState.update {  _ ->
               HomeUIState.Success(data = events.filter { it.endDate >= LocalDate.now() }.take(5))
            }
        }
    }

}