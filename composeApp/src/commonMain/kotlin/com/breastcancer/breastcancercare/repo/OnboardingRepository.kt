package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.UserDao
import com.breastcancer.breastcancercare.models.UserDTO
import com.breastcancer.breastcancercare.models.toDTO
import com.breastcancer.breastcancercare.models.toEntity
import com.breastcancer.breastcancercare.models.toLoggedInEntity
import kotlinx.coroutines.flow.map

class OnboardingRepository(val userDao: UserDao) {
    suspend fun insertUser(userDTO: UserDTO) {
        var id = userDTO.id
        if (id == 0L)
            id = getIdForUserEntity() + 1
        if (!checkIfEmailExists(userDTO.email))
            userDao.insertUser(userDTO.copy(id = id).toEntity())
        else
            throw Exception("Email already exists")

    }

    fun getUser(email: String) = userDao.getUser(email = email).map { it?.toDTO() }

    suspend fun updateUser(userDTO: UserDTO) = insertUser(userDTO)

    suspend fun getIdForUserEntity() = userDao.getMaxId()

    suspend fun checkIfEmailExists(email: String) =
        userDao.emailExistsIgnoreCase(email = email)

    suspend fun isLoggedIn() = userDao.isLoggedIn()

    fun getLoggedInUser() = userDao.getLoggedInUser().map { it?.toDTO() }

    suspend fun setLoggedInUser(userDTO: UserDTO) {
        userDao.deleteLoggedInUser()
        userDao.setLoggedInUser(userDTO.toLoggedInEntity())
    }

    suspend fun logOut() = userDao.deleteLoggedInUser()
}