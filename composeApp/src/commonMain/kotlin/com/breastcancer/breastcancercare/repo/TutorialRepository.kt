package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.UserDao

class TutorialRepository(val userDao: UserDao) {
    suspend fun updateTutorialViewedById(
        userId: Long,
        tutorialViewed: Boolean
    ) {
        userDao.updateTutorialViewedById(id = userId, tutorialViewed = tutorialViewed)
        userDao.updateLoggedInTutorialViewedById(
            id = userId,
            tutorialViewed = tutorialViewed
        )
    }
}