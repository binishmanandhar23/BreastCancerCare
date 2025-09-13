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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
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
        getAllUpcomingActivities()
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

    @OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun getAllUpcomingActivities() = viewModelScope.launch(Dispatchers.IO) {
        delay(1000L)
        _upcomingEventsUIState.update { HomeUIState.Loading() }
        homeRepository.getLoggedInUser()                 // Flow<User?>
            .map { it?.userCategory }                    // Flow<UserCategory?>
            .distinctUntilChanged()                      // don’t reload if same category
            .flatMapLatest { category ->
                if (category == null) flowOf(emptyList())    // or emit an Idle/Empty state
                else homeRepository.getAllActivities(userCategory = category) // Flow<List<Event>>
            }
            .mapLatest { events ->
                events
                    .filter { it.startDate >= LocalDate.now() }
                    .sortedBy { it.startDate }          // ensure chronological
                    .take(5)
            }.debounce(1500L)
            .onStart { _upcomingEventsUIState.value = HomeUIState.Loading() }
            .catch { e -> _upcomingEventsUIState.value = HomeUIState.Error(e.message) }
            .collectLatest { activities ->
                _upcomingEventsUIState.update { HomeUIState.Success(activities) }
            }
    }

}