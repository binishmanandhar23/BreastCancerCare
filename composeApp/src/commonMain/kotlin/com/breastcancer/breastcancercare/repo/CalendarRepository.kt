package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.CalendarDAO
import com.breastcancer.breastcancercare.models.interfaces.ProgramDTO
import com.breastcancer.breastcancercare.models.interfaces.toProgramDTO
import com.breastcancer.breastcancercare.models.toEventDTO
import com.breastcancer.breastcancercare.utils.checkIfDateHasProgram
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.LocalDate

class CalendarRepository(val calendarDAO: CalendarDAO) {
    fun getAllEvents() =
        calendarDAO.getAllEvents().map { eventEntities -> eventEntities.map { it.toEventDTO() } }

    fun getEventsFromSelectedDate(date: LocalDate) =
        calendarDAO.getEventsFromSelectedDate(date = date.toString())
            .map { eventEntities -> eventEntities.map { it.toEventDTO() } }

    suspend fun getAllProgramsFromSelectedDate(date: LocalDate, selectedDatePrograms: (MutableList<ProgramDTO>) -> Unit) {
        getAllPrograms().collect { programs ->
            val selectedDatePrograms = mutableListOf<ProgramDTO>()
            programs.forEach { program ->
                if (checkIfDateHasProgram(
                        frequencyType = program.frequency,
                        selectedDate = date,
                        startDate = program.startDate,
                        endDate = program.endDate
                    )
                )
                    selectedDatePrograms.add(program)
            }
            selectedDatePrograms(selectedDatePrograms)
        }
    }

    fun getAllPrograms() = calendarDAO.getAllPrograms().map { programEntities ->
        programEntities.map {
            it.toProgramDTO()
        }
    }


}