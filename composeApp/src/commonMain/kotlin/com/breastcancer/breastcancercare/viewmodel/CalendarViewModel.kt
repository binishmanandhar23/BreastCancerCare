package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import com.breastcancer.breastcancercare.repo.CalendarRepository
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CalendarViewModel(private val calendarRepository: CalendarRepository): ViewModel() {
    private var _selectedTab = MutableStateFlow(0)
    val selectedTab = _selectedTab.asStateFlow()

    private var _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    fun changeTab(index: Int) { _selectedTab.value = index }


    fun changeSelectedDate(date: LocalDate) { _selectedDate.value = date }
}