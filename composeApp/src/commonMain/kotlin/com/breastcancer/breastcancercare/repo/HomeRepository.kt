package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.BlogDAO
import com.breastcancer.breastcancercare.database.local.dao.CalendarDAO
import com.breastcancer.breastcancercare.database.local.dao.UserDao
import com.breastcancer.breastcancercare.models.toEventDTO
import kotlinx.coroutines.flow.map

class HomeRepository(val userDao: UserDao, val blogDAO: BlogDAO, val calendarDAO: CalendarDAO) :
    BlogRepository(blogDAO) {
    fun getLoggedInUser() = userDao.getLoggedInUser()

    fun getAllEvents() =
        calendarDAO.getAllEvents().map { events -> events.map { it.toEventDTO() } }
}