package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.types.ActivityType
import com.breastcancer.breastcancercare.database.local.types.GeneralActivityType
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.models.ActivityHistoryDTO
import com.breastcancer.breastcancercare.models.GeneralActivityDTO
import com.breastcancer.breastcancercare.models.UserDTO
import com.breastcancer.breastcancercare.repo.ActivityRepository
import com.breastcancer.breastcancercare.repo.OnboardingRepository
import com.breastcancer.breastcancercare.states.ActivityUIState
import com.breastcancer.breastcancercare.survey.model.Answer
import com.breastcancer.breastcancercare.survey.model.toPreSurveyAnswers
import com.breastcancer.breastcancercare.utils.convertMillisToLocalDate
import com.breastcancer.breastcancercare.utils.getDateForNextSession
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.seconds
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

    private var _activityUIHistoryState =
        MutableStateFlow<ActivityUIState<Map<LocalDate, List<ActivityHistoryDTO>>>>(ActivityUIState.Initial())
    val activityUIHistoryState = _activityUIHistoryState.asStateFlow()

    private var _activityUIAddState =
        MutableStateFlow<ActivityUIState<GeneralActivityDTO>>(ActivityUIState.Initial())
    val activityUIAddState = _activityUIAddState.asStateFlow()


    private var _selectedActivityType = MutableStateFlow<ActivityType?>(null)
    val selectedActivityType = _selectedActivityType.asStateFlow()

    private var _allActivityTypes = MutableStateFlow<List<ActivityType>>(emptyList())
    val allActivityTypes = _allActivityTypes.asStateFlow()

    private var _user = MutableStateFlow<UserDTO?>(null)
    val user = _user.asStateFlow()

    init {
        getLoggedInUser()
        listenForRegisterChanges()
        getAllActivitiesAndFilterByActivityType()
        getAllActivityHistoryAndFilterByActivityType()
    }

    suspend fun getActivityById(id: Long) {
        _activityUIDetailState.update { _ -> ActivityUIState.Loading() }// reset state
        activityRepository.getActivityById(id = id).let {
            _activityUIDetailState.update { _ -> ActivityUIState.Success(data = it) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class, ExperimentalTime::class)
    fun getActivityByType(type: ActivityType) =
        viewModelScope.launch {
            activityRepository.getAllActivitiesByType(activityType = type)
                .onStart {
                    _activityUIAddState.update { _ -> ActivityUIState.Loading() }// reset state
                    delay(1.seconds)
                }
                .mapLatest { activities ->
                    activities.firstOrNull()
                }
                .combine(user) { activity, user ->
                    activityRepository.getActivityHistoryByActivityId(
                        activityId = activity?.id,
                        userId = user?.id
                    )
                        .mapLatest { activityHistory ->
                            GeneralActivityDTO(
                                activityDTO = activity,
                                activityHistoryDTO = activityHistory.filter { it.registeredForDate >= LocalDate.now() }
                            )
                        }
                }.distinctUntilChanged().flatMapLatest { it }.collectLatest {
                    _activityUIAddState.update { _ ->
                        ActivityUIState.Success(
                            data = it
                        )
                    }
                }
        }

    fun updateBookingAppointment(dateInMillis: Long?) = viewModelScope.launch {
        _activityUIAddState.update { oldState ->
            ActivityUIState.Success(
                data = oldState.data?.copy(
                    appointmentDate = if (dateInMillis == null) null else convertMillisToLocalDate(
                        millis = dateInMillis
                    )
                )
            )
        }
    }

    fun selectActivityType(activityType: ActivityType?) =
        _selectedActivityType.update { activityType }

    private fun getLoggedInUser() = viewModelScope.launch {
        onboardingRepository.getLoggedInUser().collectLatest { user ->
            _user.update { user }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
    private fun getAllActivitiesAndFilterByActivityType() = viewModelScope.launch(Dispatchers.IO) {
        user.map { user ->
            if (user?.userCategory == UserCategory.StartingStrong)
                _allActivityTypes.update { StartingStrongActivityType.all + GeneralActivityType.all }
            else if (user?.userCategory == UserCategory.LivingWell)
                _allActivityTypes.update { LivingWellActivityType.all + GeneralActivityType.all }
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
                    .filter { it.dates.any { date -> date >= LocalDate.now() } }
                    .sortedBy { it.startDate }          // ensure chronological
            }
            .onStart { _activityUIListState.value = ActivityUIState.Loading() }
            .catch { e -> _activityUIListState.value = ActivityUIState.Error(e.message) }
            .collectLatest { activities ->
                _activityUIListState.update { _ ->
                    ActivityUIState.Success(data = activities)
                }
            }
    }

    @OptIn(ExperimentalTime::class)
    private fun insertActivityHistory(
        activity: ActivityDTO, registeredForDate: LocalDate = getDateForNextSession(
            frequencyType = activity.frequency,
            startDate = activity.startDate,
            endDate = activity.endDate,
            frequencySeries = activity.frequencySeries
        ) ?: LocalDate.now(), preSurveyAnswer: Map<String, Answer>? = null,
        onStart: () -> Unit = { _activityUIDetailState.update { ActivityUIState.Loading() } },
        onFinish: (activity: ActivityDTO) -> Unit = {
            _activityUIDetailState.update {
                ActivityUIState.Final(
                    data = activity
                )
            }
        }
    ) =
        viewModelScope.launch {
            onStart()
            delay(1000)
            activityRepository.insertActivityHistory(
                activityHistoryDTO = ActivityHistoryDTO(
                    activityId = activity.id,
                    userId = user.value?.id ?: 0,
                    registeredForDate = registeredForDate,
                    preSurveyAnswers = preSurveyAnswer.toPreSurveyAnswers()
                )
            )
            onFinish(activity)
        }

    @OptIn(ExperimentalTime::class)
    fun insertActivityHistoryForActivityDetail(
        activity: ActivityDTO, registeredForDate: LocalDate = getDateForNextSession(
            frequencyType = activity.frequency,
            startDate = activity.startDate,
            endDate = activity.endDate,
            frequencySeries = activity.frequencySeries
        ) ?: LocalDate.now(), preSurveyAnswer: Map<String, Answer>? = null,
        onStart: () -> Unit = { _activityUIDetailState.update { ActivityUIState.Loading() } },
        onFinish: (activity: ActivityDTO) -> Unit = {
            _activityUIDetailState.update {
                ActivityUIState.Final(
                    data = activity
                )
            }
        }
    ) = insertActivityHistory(
        activity = activity,
        registeredForDate = registeredForDate,
        preSurveyAnswer = preSurveyAnswer,
        onStart = onStart,
        onFinish = onFinish
    )

    @OptIn(ExperimentalTime::class)
    fun insertActivityHistoryForAddActivity(
        activity: ActivityDTO,
        activityHistories: List<ActivityHistoryDTO>,
        registeredForDate: LocalDate,
        preSurveyAnswer: Map<String, Answer>? = null,
        onStart: () -> Unit = { _activityUIAddState.update { ActivityUIState.Loading() } }
    ) =
        insertActivityHistory(
            activity = activity,
            registeredForDate = registeredForDate,
            preSurveyAnswer = preSurveyAnswer,
            onStart = onStart,
            onFinish = {
                _activityUIAddState.update {
                    ActivityUIState.Final(
                        data = GeneralActivityDTO(
                            activityDTO = activity,
                            activityHistoryDTO = activityHistories,
                            appointmentDate = registeredForDate
                        )
                    )
                }
            }
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun listenForRegisterChanges() = viewModelScope.launch(Dispatchers.IO) {
        combine(activityUIDetailState, user) { state, user ->
            Pair((state as? ActivityUIState.Success)?.data, user)
        }.distinctUntilChanged().filter { (activity, _) -> activity != null }
            .flatMapLatest { (activity, user) ->
                val nextSession = getDateForNextSession(
                    frequencyType = activity!!.frequency,
                    startDate = activity.startDate,
                    endDate = activity.endDate,
                    frequencySeries = activity.frequencySeries
                )
                activityRepository.getActivityHistoryByActivityIdAndRegisteredDate(
                    activityId = activity.id,
                    userId = user?.id,
                    registeredDate = nextSession
                ).map { activityHistory -> activity to activityHistory }
            }.collectLatest { (activity, activityHistory) ->
                activityHistory?.let {
                    _activityUIDetailState.update { ActivityUIState.Final(data = activity) }
                }
            }
    }

    @OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
    private fun getAllActivityHistoryAndFilterByActivityType() =
        viewModelScope.launch(Dispatchers.IO) {
            user.mapLatest { user ->
                user?.id
            }
                .distinctUntilChanged()
                .mapLatest { userId ->
                    activityRepository.getAllActivityHistoryWithActivity(userId = userId)
                }.flatMapLatest { activityHistory -> activityHistory }
                .combine(selectedActivityType) { activityHistory, type ->
                    if (type == null) activityHistory.groupBy { it.registeredForDate }
                    else
                        activityHistory.filter { it.activity?.activityType?.type == type.type }
                            .groupBy { it.registeredForDate }
                }
                .onStart { _activityUIHistoryState.value = ActivityUIState.Loading() }
                .catch { e -> _activityUIHistoryState.value = ActivityUIState.Error(e.message) }
                .collectLatest { activityHistory ->
                    _activityUIHistoryState.update { _ ->
                        ActivityUIState.Success(data = activityHistory)
                    }
                }
        }
}