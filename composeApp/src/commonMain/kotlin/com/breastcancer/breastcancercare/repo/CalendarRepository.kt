package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.CalendarDAO
import com.breastcancer.breastcancercare.models.interfaces.toProgramDTO
import com.breastcancer.breastcancercare.models.toEventDTO
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class CalendarRepository(val calendarDAO: CalendarDAO) {
    fun getAllEvents() =
        calendarDAO.getAllEvents().map { eventEntities -> eventEntities.map { it.toEventDTO() } }

    fun getEventsFromSelectedDate(date: LocalDate) = calendarDAO.getEventsFromSelectedDate(date = date.toString())
        .map { eventEntities -> eventEntities.map { it.toEventDTO() } }

    fun getAllPrograms() = calendarDAO.getAllPrograms().map { programEntities -> programEntities.map { it.toProgramDTO() } }

    fun getProgramsFromSelectedDate(date: LocalDate) = calendarDAO.getProgramsFromSelectedDate(date = date.toString()).map { programEntities -> programEntities.map { it.toProgramDTO() } }
}