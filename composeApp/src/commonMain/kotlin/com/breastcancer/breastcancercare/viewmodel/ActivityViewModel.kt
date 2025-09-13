package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.types.ActivityType
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.repo.ActivityRepository
import com.breastcancer.breastcancercare.repo.OnboardingRepository
import com.breastcancer.breastcancercare.states.ActivityUIState
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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

class ActivityViewModel(
    val activityRepository: ActivityRepository,
    val onboardingRepository: OnboardingRepository
) : ViewModel() {
    private var _activityUIDetailState =
        MutableStateFlow<ActivityUIState<ActivityDTO>>(ActivityUIState.Initial())
    val activityUIDetailState = _activityUIDetailState.asStateFlow()

    private var _activityUIListState =
        MutableStateFlow<ActivityUIState<List<ActivityDTO>>>(ActivityUIState.Initial())
    val activityUIListState = _activityUIListState.asStateFlow()

    private var _selectedActivityType = MutableStateFlow<ActivityType?>(null)
    val selectedActivityType = _selectedActivityType.asStateFlow()

    private var _allActivityTypes = MutableStateFlow<List<ActivityType>>(emptyList())
    val allActivityTypes = _allActivityTypes.asStateFlow()

    init {
        getAllActivitiesAndFilterByActivityType()
    }

    suspend fun getActivityById(id: Long) {
        _activityUIDetailState.update { _ -> ActivityUIState.Loading() }// reset state
        activityRepository.getActivityById(id = id).let {
            _activityUIDetailState.update { _ -> ActivityUIState.Success(data = it) }
        }
    }

    fun selectActivityType(activityType: ActivityType?) =
        _selectedActivityType.update { activityType }

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
    private fun getAllActivitiesAndFilterByActivityType() = viewModelScope.launch(Dispatchers.IO) {
        onboardingRepository.getLoggedInUser()                 // Flow<User?>
            .map { user ->
                if (user?.userCategory == UserCategory.StartingStrong)
                    _allActivityTypes.update { StartingStrongActivityType.all }
                else if (user?.userCategory == UserCategory.LivingWell)
                    _allActivityTypes.update { LivingWellActivityType.all }
                user?.userCategory
            }
            .distinctUntilChanged()
            .combine(selectedActivityType) { category, type ->
                if (category == null) flowOf(emptyList())    // or emit an Idle/Empty state
                else if (type == null) activityRepository.getAllActivities(userCategory = category)
                else activityRepository.getAllActivitiesByType(
                    activityType = type
                )
            }.flatMapLatest { activities -> activities }.mapLatest { activities ->
                // compute "today" once (in the user’s local zone)
                activities
                    .filter { it.startDate >= LocalDate.now() }
                    .sortedBy { it.startDate }          // ensure chronological
            }
            .onStart { _activityUIListState.value = ActivityUIState.Loading() }
            .catch { e -> _activityUIListState.value = ActivityUIState.Error(e.message) }
            .collectLatest { activities ->
                _activityUIListState.update { _ ->
                    ActivityUIState.Success(data = activities.filter { activity -> activity.startDate >= LocalDate.now() }
                        .sortedBy { activity -> activity.startDate })
                }
            }
    }
}