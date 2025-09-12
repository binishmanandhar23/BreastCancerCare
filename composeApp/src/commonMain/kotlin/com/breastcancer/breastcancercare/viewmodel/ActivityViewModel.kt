package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.repo.ActivityRepository
import com.breastcancer.breastcancercare.states.ActivityUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ActivityViewModel(val activityRepository: ActivityRepository): ViewModel() {
    private var _activityUIDetailState = MutableStateFlow<ActivityUIState<ActivityDTO>>(ActivityUIState.Initial())
    val activityUIDetailState = _activityUIDetailState.asStateFlow()

    private var _activityUIListState =
        MutableStateFlow<ActivityUIState<List<ActivityDTO>>>(ActivityUIState.Initial())
    val activityUIListState = _activityUIListState.asStateFlow()

    suspend fun getActivityById(id: Long) {
        _activityUIDetailState.update { _ -> ActivityUIState.Loading() }// reset state
        activityRepository.getActivityById(id = id).let {
            _activityUIDetailState.update { _ -> ActivityUIState.Success(data = it) }
        }
    }
}