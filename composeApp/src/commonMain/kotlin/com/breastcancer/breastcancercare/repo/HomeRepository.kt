package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.BlogDAO
import com.breastcancer.breastcancercare.database.local.dao.UserDao

class HomeRepository(val userDao: UserDao, val blogDAO: BlogDAO): BlogRepository(blogDAO) {
    fun getLoggedInUser() = userDao.getLoggedInUser()


}