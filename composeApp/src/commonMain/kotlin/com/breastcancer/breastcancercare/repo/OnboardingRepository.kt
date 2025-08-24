package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.OnboardingDAO
import com.breastcancer.breastcancercare.models.UserDTO
import com.breastcancer.breastcancercare.models.toDTO
import com.breastcancer.breastcancercare.models.toEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

class OnboardingRepository(val onboardingDAO: OnboardingDAO) {
    suspend fun insertUser(userDTO: UserDTO) {
        var id = userDTO.id
        if (id == 0L)
            id = getIdForUserEntity() + 1
        if (!checkIfEmailExists(userDTO.email))
            onboardingDAO.insertUser(userDTO.copy(id = id).toEntity())
        else
            throw Exception("Email already exists")

    }

    fun getUser(email: String) = onboardingDAO.getUser(email = email).map { it?.toDTO() }

    suspend fun updateUser(userDTO: UserDTO) = insertUser(userDTO)

    suspend fun getIdForUserEntity() = onboardingDAO.getMaxId()

    suspend fun checkIfEmailExists(email: String) = onboardingDAO.emailExistsIgnoreCase(email = email)

    suspend fun isLoggedIn() = onboardingDAO.isLoggedIn()

    fun getLoggedInUser() = onboardingDAO.getLoggedInUser().map { it.toDTO() }
}