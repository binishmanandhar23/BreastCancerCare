package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.BlogDAO
import com.breastcancer.breastcancercare.database.local.dao.ActivityDAO
import com.breastcancer.breastcancercare.database.local.dao.UserDao
import com.breastcancer.breastcancercare.database.local.entity.LoggedInUserEntity
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.UserDTO
import com.breastcancer.breastcancercare.models.toActivityDTO
import com.breastcancer.breastcancercare.models.toDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

class HomeRepository(val userDao: UserDao, val activityDAO: ActivityDAO) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLoggedInUser(): Flow<UserDTO?> = userDao.getLoggedInUser().distinctUntilChanged().map { it?.toDTO() }

    fun getAllActivities(userCategory: UserCategory) =
        activityDAO.getAllActivities(userCategory = userCategory.category)
            .map { events -> events.map { it.toActivityDTO() } }

    suspend fun updateUserCategoryById(
        userId: Long,
        userCategory: UserCategory
    ) {
        userDao.updateUserCategoryById(id = userId, userCategory = userCategory.category)
        userDao.updateLoggedInUserCategoryById(
            id = userId,
            userCategory = userCategory.category
        )
    }
}