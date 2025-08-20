package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.OnboardingDAO
import com.breastcancer.breastcancercare.models.UserDTO
import com.breastcancer.breastcancercare.models.toDTO
import com.breastcancer.breastcancercare.models.toEntity
import kotlinx.coroutines.flow.map

class OnboardingRepository(val onboardingDAO: OnboardingDAO) {
    suspend fun insertUser(userDTO: UserDTO) = onboardingDAO.insertUser(userDTO.toEntity())

    fun getUser(email: String) = onboardingDAO.getUser(email = email).map { it?.toDTO() }

    suspend fun updateUser(userDTO: UserDTO) = insertUser(userDTO)
}