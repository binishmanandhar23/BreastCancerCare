package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.BlogDAO
import com.breastcancer.breastcancercare.database.local.dao.ActivityDAO
import com.breastcancer.breastcancercare.database.local.dao.UserDao
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.toActivityDTO
import com.breastcancer.breastcancercare.models.toDTO
import kotlinx.coroutines.flow.map

class HomeRepository(val userDao: UserDao, val blogDAO: BlogDAO, val activityDAO: ActivityDAO) :
    BlogRepository(blogDAO) {
    fun getLoggedInUser() = userDao.getLoggedInUser().map { it?.toDTO() }

    fun getAllActivities(userCategory: UserCategory) =
        activityDAO.getAllActivities(userCategory = userCategory.category).map { events -> events.map { it.toActivityDTO() } }
}