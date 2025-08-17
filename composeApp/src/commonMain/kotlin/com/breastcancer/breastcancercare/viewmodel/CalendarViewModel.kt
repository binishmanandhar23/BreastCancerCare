package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.entity.EventEntity
import com.breastcancer.breastcancercare.database.local.types.EventType
import com.breastcancer.breastcancercare.models.EventDTO
import com.breastcancer.breastcancercare.models.toEventDTO
import com.breastcancer.breastcancercare.repo.CalendarRepository
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CalendarViewModel(private val calendarRepository: CalendarRepository) : ViewModel() {
    private var _selectedTab = MutableStateFlow(0)
    val selectedTab = _selectedTab.asStateFlow()

    private var _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private var _allEvents = MutableStateFlow<List<EventDTO>>(emptyList())
    val allEvents = _allEvents.asStateFlow()

    private var _selectedDayEvents = MutableStateFlow<List<EventDTO>>(emptyList())
    val selectedDayEvents = _selectedDayEvents.asStateFlow()

    init {
        populateData()
        getAllEventsAndPrograms()
        getAllEventsOnSelectedDate()
    }

    fun changeTab(index: Int) {
        _selectedTab.value = index
    }


    fun changeSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun getAllEventsAndPrograms() =
        viewModelScope.launch(Dispatchers.IO) {
            calendarRepository.calendarDAO.getAllEvents().collect { events ->
                _allEvents.value = events.map { event -> event.toEventDTO() }
            }
        }

    private fun getAllEventsOnSelectedDate() = viewModelScope.launch(Dispatchers.IO){
        selectedDate.collectLatest { date ->
            calendarRepository.calendarDAO.getEventsFromSelectedDate(date.toString())
                .collect { events ->
                    _selectedDayEvents.value = events.map { event -> event.toEventDTO() }
                }
        }
    }


    private fun populateData() {
        viewModelScope.launch {
            calendarRepository.calendarDAO.insertAllEvents(
                listOf(
                    EventEntity(
                        id = 1,
                        name = "Metastatic Support Group",
                        description = "The support group is for women living with metastatic breast cancer",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 17).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Joondalup, Western Australia, Australia",
                        isFeatured = false,
                    )
                )
            )
        }
    }
}