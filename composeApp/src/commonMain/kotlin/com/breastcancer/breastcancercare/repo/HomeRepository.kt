package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.BlogDAO
import com.breastcancer.breastcancercare.database.local.dao.ActivityDAO
import com.breastcancer.breastcancercare.database.local.dao.UserDao
import com.breastcancer.breastcancercare.models.toActivityDTO
import kotlinx.coroutines.flow.map

class HomeRepository(val userDao: UserDao, val blogDAO: BlogDAO, val activityDAO: ActivityDAO) :
    BlogRepository(blogDAO) {
    fun getLoggedInUser() = userDao.getLoggedInUser()

    fun getAllEvents() =
        activityDAO.getAllEvents().map { events -> events.map { it.toActivityDTO() } }
}