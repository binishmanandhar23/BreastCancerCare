package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.types.FrequencyType
import com.breastcancer.breastcancercare.models.EventDTO
import com.breastcancer.breastcancercare.models.interfaces.ProgramDTO
import com.breastcancer.breastcancercare.repo.CalendarRepository
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
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

    private var _allDatesWithPrograms = MutableStateFlow<List<String>>(emptyList())
    val allDatesWithPrograms = _allDatesWithPrograms.asStateFlow()

    init {
        getAllEventsAndPrograms()
        getAllEventsOnSelectedDate()
        getAllProgramsOnSelectedDate()
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
    }.also {
        viewModelScope.launch(Dispatchers.IO) {
            calendarRepository.getAllPrograms().collect { programs ->
                _allPrograms.update { programs }
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

    private fun getAllProgramsOnSelectedDate() = viewModelScope.launch(Dispatchers.IO) {
        selectedDate.collectLatest { date ->
            calendarRepository.getAllProgramsFromSelectedDate(date = date) { programs ->
                _selectedDayPrograms.update { programs }
            }
        }
    }


    private fun findAllDatesWithEventsAndPrograms() {
        viewModelScope.launch(Dispatchers.IO) {
            allEvents.collect { events ->
                _allDatesWithEvents.update { events.map { it.date.toString() }.distinct() }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            allPrograms.collect { programs ->
                val dates = mutableListOf<LocalDate>()
                programs.forEach { program ->
                    when (program.frequency) {
                        FrequencyType.Daily -> {
                            var date = program.startDate
                            var i = 0
                            while (date <= program.endDate) {
                                date = program.startDate.plusDays(i)
                                dates.add(date)
                                i++
                            }
                        }

                        FrequencyType.Weekly -> {
                            var date = program.startDate
                            var i = 0
                            while (date <= program.endDate) {
                                date = program.startDate.plus(i, DateTimeUnit.WEEK)
                                dates.add(date)
                                i++
                            }
                        }

                        FrequencyType.Monthly -> {
                            var date = program.startDate
                            var i = 0
                            while (date <= program.endDate) {
                                date = program.startDate.plus(i, DateTimeUnit.MONTH)
                                dates.add(date)
                                i++
                            }
                        }

                        FrequencyType.Yearly -> {
                            var date = program.startDate
                            var i = 0
                            while (date <= program.endDate) {
                                date = program.startDate.plus(i, DateTimeUnit.YEAR)
                                dates.add(date)
                                i++
                            }
                        }
                    }

                }
                _allDatesWithPrograms.update { dates.map { it.toString() }.distinct() }
            }
        }
    }
}