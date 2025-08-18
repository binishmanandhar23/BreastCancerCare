package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.entity.EventEntity
import com.breastcancer.breastcancercare.database.local.types.EventType
import com.breastcancer.breastcancercare.models.EventDTO
import com.breastcancer.breastcancercare.models.interfaces.ProgramDTO
import com.breastcancer.breastcancercare.repo.CalendarRepository
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CalendarViewModel(private val calendarRepository: CalendarRepository) : ViewModel() {
    private var _selectedTab = MutableStateFlow(0)
    val selectedTab = _selectedTab.asStateFlow()

    private var _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private var _allEvents = MutableStateFlow<List<EventDTO>>(emptyList())
    val allEvents = _allEvents.asStateFlow()

    private var _allPrograms = MutableStateFlow<List<ProgramDTO>>(emptyList())
    val allPrograms = _allPrograms.asStateFlow()

    private var _selectedDayEvents = MutableStateFlow<List<EventDTO>>(emptyList())
    val selectedDayEvents = _selectedDayEvents.asStateFlow()

    private var _selectedDayPrograms = MutableStateFlow<List<ProgramDTO>>(emptyList())
    val selectedDayPrograms = _selectedDayPrograms.asStateFlow()

    private var _allDatesWithEvents = MutableStateFlow<List<String>>(emptyList())
    val allDatesWithEvents = _allDatesWithEvents.asStateFlow()

    private var _allDatesWithPrograms= MutableStateFlow<List<String>>(emptyList())
    val allDatesWithPrograms = _allDatesWithPrograms.asStateFlow()

    init {
        populateData()
        getAllEventsAndPrograms()
        getAllEventsOnSelectedDate()
        findAllDatesWithEventsAndPrograms()
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
        }
        calendarRepository.getAllPrograms().collect { programs ->
            _allPrograms.update {  programs }
        }
    }

    private fun getAllEventsOnSelectedDate() = viewModelScope.launch(Dispatchers.IO) {
        selectedDate.collectLatest { date ->
            calendarRepository.getEventsFromSelectedDate(date = date)
                .collect { events ->
                    _selectedDayEvents.update { events }
                }
            calendarRepository.getProgramsFromSelectedDate(date = date).collect { programs ->
                _selectedDayPrograms.update { programs }
            }
        }
    }


    private fun populateData() {
        viewModelScope.launch {
            calendarRepository.calendarDAO.insertAllEvents(
                listOf(
                    EventEntity(
                        id = 1,
                        name = "Early Breast Cancer Group – Mandurah",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 19).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Mandurah, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 2,
                        name = "Young Women’s Early Breast Cancer Group – Online",
                        description = "This support group is for women with early breast cancer that are under 45 or with school aged children.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 19).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = true,
                        location = null, // ⬅️ online
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 3,
                        name = "Online Partners of Women with Metastatic Breast Cancer Support Group",
                        description = "This group is for partners of women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 21).toString(),
                        startTime = LocalTime(19, 0, 0).toString(),
                        endTime = LocalTime(20, 0, 0).toString(),
                        isOnline = false,
                        location = "Cottesloe, Western Australia, Australia",
                        isFeatured = true,
                    ),
                    EventEntity(
                        id = 4,
                        name = "Young Women’s Early Breast Cancer Group – Hamersley",
                        description = "This support group is for women with early breast cancer that are under 45 or with school aged children.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 22).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Hamersley, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 5,
                        name = "Young Women’s Metastatic Support Group – Online",
                        description = "This support group is for women living with metastatic breast cancer who are under 45 or have school-aged children.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 26).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = true,
                        location = null, // ⬅️ online
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 6,
                        name = "Early Breast Cancer Group – Midland",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 28).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Midland, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 7,
                        name = "Early Breast Cancer Group – Online",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 2).toString(),
                        startTime = LocalTime(12, 30, 0).toString(),
                        endTime = LocalTime(13, 30, 0).toString(),
                        isOnline = true,
                        location = null, // ⬅️ online
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 8,
                        name = "Metastatic Support Group – Cottesloe",
                        description = "This support group is for women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 3).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Cottesloe, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 9,
                        name = "Metastatic Support Group – Mandurah",
                        description = "This support group is for women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 10).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Mandurah, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 10,
                        name = "Metastatic Support Group – Online",
                        description = "This support group is for women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 10).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = true,
                        location = null, // ⬅️ online
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 11,
                        name = "Young Women’s Early Breast Cancer Group – Murdoch",
                        description = "This support group is for women with early breast cancer that are under 45 or with school aged children.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 11).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Murdoch, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 12,
                        name = "Metastatic Support Group – Bunbury",
                        description = "This support group is for women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 11).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Bunbury, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 13,
                        name = "Living Well Discussion Group – Online",
                        description = "Discussion group for women who have recently completed treatment for early breast cancer; topics include fear of recurrence, side effects, stress management, healthy lifestyle and goal setting.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 11).toString(),
                        startTime = LocalTime(19, 0, 0).toString(),
                        endTime = LocalTime(20, 0, 0).toString(),
                        isOnline = true,
                        location = null, // ⬅️ online
                        isFeatured = true,
                    ),
                    EventEntity(
                        id = 14,
                        name = "Early Breast Cancer Group – Hamersley",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 12).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Hamersley, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 15,
                        name = "Metastatic Support Group – Joondalup",
                        description = "This support group is for women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 19).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Joondalup, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 16,
                        name = "Living Well Early Breast Cancer Support Group – Bunbury",
                        description = "Group for women living well after treatment; Thursdays 7–8pm covering fear of recurrence, side effects, stress management, healthy lifestyle and goal setting.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 10, 2).toString(),
                        startTime = LocalTime(19, 0, 0).toString(),
                        endTime = LocalTime(20, 0, 0).toString(),
                        isOnline = false,
                        location = "Bunbury, Western Australia, Australia",
                        isFeatured = true,
                    ),
                )

            )
        }
    }

    private fun findAllDatesWithEventsAndPrograms() = viewModelScope.launch(Dispatchers.IO) {
        allEvents.collect { events ->
            _allDatesWithEvents.update { events.map { it.date.toString() }.distinct() }
        }
    }.also {
        viewModelScope.launch(Dispatchers.IO){
            allPrograms.collect { programs ->
                _allDatesWithPrograms.update { programs.map { it.date.toString() }.distinct() }
            }
        }
    }
}