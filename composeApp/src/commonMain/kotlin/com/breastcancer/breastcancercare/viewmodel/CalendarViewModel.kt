package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.models.ActivityDTO
import com.breastcancer.breastcancercare.models.SuitabilityDTO
import com.breastcancer.breastcancercare.notifications.NotificationChannels
import com.breastcancer.breastcancercare.notifications.createAlarmeePlatformConfiguration
import com.breastcancer.breastcancercare.repo.CalendarRepository
import com.kizitonwose.calendar.core.now
import com.tweener.alarmee.createAlarmeeService
import com.tweener.alarmee.model.Alarmee
import com.tweener.alarmee.model.AndroidNotificationConfiguration
import com.tweener.alarmee.model.AndroidNotificationPriority
import com.tweener.alarmee.model.IosNotificationConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import kotlin.math.max
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CalendarViewModel(private val calendarRepository: CalendarRepository) : ViewModel() {

    val alarmeeService = createAlarmeeService()

    private var _selectedTab = MutableStateFlow(0)
    val selectedTab = _selectedTab.asStateFlow()

    private var _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private var _allEvents = MutableStateFlow<List<ActivityDTO>>(emptyList())
    val allEvents = _allEvents.asStateFlow()

    private var _selectedDayEvents = MutableStateFlow<List<ActivityDTO>>(emptyList())
    val selectedDayEvents = _selectedDayEvents.asStateFlow()

    private var _allDatesWithEvents = MutableStateFlow<List<String>>(emptyList())
    val allDatesWithEvents = _allDatesWithEvents.asStateFlow()

    private var _allDatesWithPrograms = MutableStateFlow<List<String>>(emptyList())
    val allDatesWithPrograms = _allDatesWithPrograms.asStateFlow()

    private var _allSuitabilities = MutableStateFlow<List<SuitabilityDTO>>(emptyList())
    val allSuitabilities = _allSuitabilities.asStateFlow()

    private var _selectedSuitability = MutableStateFlow<SuitabilityDTO?>(null)
    val selectedSuitability = _selectedSuitability.asStateFlow()

    init {
        configureNotifications()
        getAllEventsAndPrograms()
        getAllEventsOnSelectedDate()
        getAllSuitabilities()
        findAllDatesWithEventsAndPrograms()
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

    fun getAllEventsAndPrograms() = viewModelScope.launch(Dispatchers.IO) {
        calendarRepository.getAllEvents().collect { events ->
            _allEvents.update { events }
            scheduleNotificationsForEvents(events = events)
        }
    }

    private fun scheduleNotificationsForEvents(events: List<ActivityDTO>) {
        val localService = alarmeeService.local
        events.forEach { event ->
            run NotificationLogic@ {
                if (event.endDate < LocalDate.now()) return@NotificationLogic
                val scheduledDateTime =
                    if (event.startTime != null) event.endDate.atTime(
                        LocalTime(
                            hour = max(
                                0,
                                event.startTime.hour - 1
                            ), minute = event.startTime.minute
                        )
                    ) else event.endDate.atTime(
                        6,
                        0
                    )
                localService.schedule(
                    alarmee = Alarmee(
                        uuid = event.id.toString(),
                        notificationTitle = "You have an event today!",
                        notificationBody = if (event.startTime != null) "Your event starts at ${event.startTime}" else "",
                        scheduledDateTime = scheduledDateTime,
//                    deepLinkUri = "https://www.example.com", // A deep link URI to be retrieved in MainActivity#onNewIntent() on Android and in AppDelegate#userNotificationCenter() on iOS
                        androidNotificationConfiguration = AndroidNotificationConfiguration(
                            // Required configuration for Android target only (this parameter is ignored on iOS)
                            priority = AndroidNotificationPriority.HIGH,
                            channelId = NotificationChannels.EventNotificationChannel.channelId,
                        ),
                        iosNotificationConfiguration = IosNotificationConfiguration(),
                    )
                )
            }
        }
    }

    private fun getAllEventsOnSelectedDate() = viewModelScope.launch(Dispatchers.IO) {
        selectedDate.collectLatest { date ->
            calendarRepository.getEventsFromSelectedDate(date = date)
                .collect { events ->
                    _selectedDayEvents.update { events }
                }
        }
    }



    private fun findAllDatesWithEventsAndPrograms() {
        viewModelScope.launch(Dispatchers.IO) {
            allEvents.collect { events ->
                _allDatesWithEvents.update { events.map { it.endDate.toString() }.distinct() }
            }
        }
    }

    private fun getAllSuitabilities() = viewModelScope.launch(Dispatchers.IO) {
        calendarRepository.getAllSuitabilities().collect { suitabilities ->
            _allSuitabilities.update { suitabilities }
        }
    }

    fun updateSelectedSuitability(suitabilityDTO: SuitabilityDTO?) =
        _selectedSuitability.update { suitabilityDTO }
}