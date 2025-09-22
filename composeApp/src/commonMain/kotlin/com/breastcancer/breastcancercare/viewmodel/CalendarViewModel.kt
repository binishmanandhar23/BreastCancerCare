package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.models.ActivityHistoryDTO
import com.breastcancer.breastcancercare.models.CalendarActivityType
import com.breastcancer.breastcancercare.models.SuitabilityDTO
import com.breastcancer.breastcancercare.models.UserDTO
import com.breastcancer.breastcancercare.notifications.NotificationChannels
import com.breastcancer.breastcancercare.notifications.createAlarmeePlatformConfiguration
import com.breastcancer.breastcancercare.repo.ActivityRepository
import com.breastcancer.breastcancercare.repo.HomeRepository
import com.kizitonwose.calendar.core.now
import com.tweener.alarmee.createAlarmeeService
import com.tweener.alarmee.model.Alarmee
import com.tweener.alarmee.model.AndroidNotificationConfiguration
import com.tweener.alarmee.model.AndroidNotificationPriority
import com.tweener.alarmee.model.IosNotificationConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import kotlin.math.max
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CalendarViewModel(
    private val activityRepository: ActivityRepository,
    private val homeRepository: HomeRepository
) : ViewModel() {

    val alarmeeService = createAlarmeeService()

    private var _selectedTab = MutableStateFlow(0)
    val selectedTab = _selectedTab.asStateFlow()

    private var _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private var _allActivities = MutableStateFlow<List<ActivityDTO>>(emptyList())
    val allActivities = _allActivities.asStateFlow()

    private var _allActivityHistory = MutableStateFlow<List<ActivityHistoryDTO>>(emptyList())
    val allActivityHistory = _allActivityHistory.asStateFlow()

    private var _selectedDayAvailableActivities =
        MutableStateFlow<Map<CalendarActivityType, List<ActivityDTO>>>(emptyMap())
    val selectedDayAvailableActivities = _selectedDayAvailableActivities.asStateFlow()

    private var _allDatesWithActivitiesAvailable = MutableStateFlow<List<String>>(emptyList())
    val allDatesWithActivitiesAvailable = _allDatesWithActivitiesAvailable.asStateFlow()

    private var _allDatesWithActivitiesHistory = MutableStateFlow<List<String>>(emptyList())
    val allDatesWithActivitiesHistory = _allDatesWithActivitiesHistory.asStateFlow()

    private var _allDatesWithPrograms = MutableStateFlow<List<String>>(emptyList())
    val allDatesWithPrograms = _allDatesWithPrograms.asStateFlow()

    private var _allSuitabilities = MutableStateFlow<List<SuitabilityDTO>>(emptyList())
    val allSuitabilities = _allSuitabilities.asStateFlow()

    private var _selectedSuitability = MutableStateFlow<SuitabilityDTO?>(null)
    val selectedSuitability = _selectedSuitability.asStateFlow()

    private var _user = MutableStateFlow<UserDTO?>(null)
    val user = _user.asStateFlow()

    init {
        getLoggedInUser()
        configureNotifications()
        getAllEventsAndPrograms()
        getAllActivityHistory()
        getAllEventsOnSelectedDate()
        getAllSuitabilities()
        findAllDatesWithActivitiesAvailableAndRegistered()
    }

    fun configureNotifications() {
        alarmeeService.initialize(platformConfiguration = createAlarmeePlatformConfiguration())
    }

    fun changeTab(index: Int) {
        _selectedTab.update { index }
    }


    fun changeSelectedDate(date: LocalDate) {
        _selectedDate.update { date }
    }

    fun getLoggedInUser() =
        homeRepository.getLoggedInUser().onEach { user ->
            _user.update { user }
        }.launchIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllEventsAndPrograms() = viewModelScope.launch(Dispatchers.IO) {
        user                // Flow<User?>
            .mapLatest { it?.userCategory }                    // Flow<UserCategory?>
            .distinctUntilChanged()                      // don’t reload if same category
            .flatMapLatest { category ->
                if (category == null) flowOf(emptyList())    // or emit an Idle/Empty state
                else activityRepository.getAllActivities(userCategory = category) // Flow<List<Event>>
            }
            .mapLatest { events ->
                events.sortedBy { it.startDate }          // ensure chronological
            }.collectLatest { activities ->
                _allActivities.update { activities }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllActivityHistory() = viewModelScope.launch(Dispatchers.IO) {
        user.mapLatest { it?.id }
            .distinctUntilChanged()
            .flatMapLatest { userId ->
                if (userId == null) flowOf(emptyList())
                else activityRepository.getAllActivityHistoryWithActivity(userId = userId)
            }.collectLatest { activityHistory ->
                _allActivityHistory.update { activityHistory }
            }
    }

    private fun scheduleNotificationsForEvents(activityHistory: List<ActivityHistoryDTO>) {
        val localService = alarmeeService.local
        activityHistory.map { Triple(it.id, it.registeredForDate, it.activity) }
            .forEach { (id, registeredDate, activity) ->
                run NotificationLogic@{
                    if (registeredDate < LocalDate.now()) return@NotificationLogic
                    val scheduledDateTime =
                        if (activity?.startTime != null) registeredDate.atTime(
                            LocalTime(
                                hour = max(
                                    0,
                                    activity.startTime.hour - 1
                                ), minute = activity.startTime.minute
                            )
                        ) else registeredDate.atTime(
                            6,
                            0
                        )
                    localService.schedule(
                        alarmee = Alarmee(
                            uuid = id.toString(),
                            notificationTitle = "You have an event today!",
                            notificationBody = if (activity?.startTime != null) "Your event starts at ${activity.startTime}" else "",
                            scheduledDateTime = scheduledDateTime,
//                    deepLinkUri = "https://www.example.com", // A deep link URI to be retrieved in MainActivity#onNewIntent() on Android and in AppDelegate#userNotificationCenter() on iOS
                            androidNotificationConfiguration = AndroidNotificationConfiguration(
                                // Required configuration for Android target only (this parameter is ignored on iOS)
                                priority = AndroidNotificationPriority.HIGH,
                                channelId = NotificationChannels.ActivityNotificationChannel.channelId,
                            ),
                            iosNotificationConfiguration = IosNotificationConfiguration(),
                        )
                    )
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getAllEventsOnSelectedDate() = viewModelScope.launch(Dispatchers.IO) {
        combine(
            selectedDate,
            allActivities,
            allActivityHistory
        ) { selectedDate, allActivities, allActivityHistory ->
            Triple(selectedDate, allActivities, allActivityHistory)
        }.mapLatest { (selectedDate, activities, allActivityHistory) ->
            activities.filter { activity ->
                activity.dates.contains(
                    selectedDate
                )
            }.let { activities ->
                val registered = activities.filter { activity ->
                    allActivityHistory.find { it.activityId == activity.id && it.registeredForDate == selectedDate } != null
                }
                val available =
                    activities.filter { activity -> allActivityHistory.find { it.activityId == activity.id && it.registeredForDate == selectedDate } == null }
                mapOf(
                    CalendarActivityType.Registered to registered,
                    CalendarActivityType.Available to available,
                )
            }
        }.collectLatest { activities ->
            _selectedDayAvailableActivities.update {
                activities
            }
        }
    }


    private fun findAllDatesWithActivitiesAvailableAndRegistered() {
        viewModelScope.launch(Dispatchers.IO) {
            combine(allActivities, allActivityHistory) { activities, activityHistory ->
                Pair(activities, activityHistory)
            }.collectLatest { (activities, activityHistory) ->
                _allDatesWithActivitiesHistory.update {
                    activityHistory.map { it.registeredForDate.toString() }.distinct()
                }
                scheduleNotificationsForEvents(activityHistory = activityHistory)
                val availableDates = mutableListOf<String>()
                activities.flatMap { it.dates }.distinct().forEach { date ->
                    val available =
                        activities.filter { it.dates.contains(date) }
                            .filter { activity -> activityHistory.find { it.activityId == activity.id && it.registeredForDate == date } == null }
                    if (available.isNotEmpty())
                        availableDates.add(date.toString())
                }
                _allDatesWithActivitiesAvailable.update {
                    availableDates.distinct()
                }
            }
        }
    }

    private fun getAllSuitabilities() = viewModelScope.launch(Dispatchers.IO) {
        activityRepository.getAllSuitabilities().collect { suitabilities ->
            _allSuitabilities.update { suitabilities }
        }
    }

    fun updateSelectedSuitability(suitabilityDTO: SuitabilityDTO?) =
        _selectedSuitability.update { suitabilityDTO }
}