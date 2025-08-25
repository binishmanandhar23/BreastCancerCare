package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.UserDao

class HomeRepository(val userDao: UserDao) {
    fun getLoggedInUser() = userDao.getLoggedInUser()
}