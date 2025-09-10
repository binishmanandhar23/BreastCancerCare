package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.LoggedInUserEntity
import com.breastcancer.breastcancercare.database.local.entity.UserEntity
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.interfaces.UserInterface

data class UserDTO(
    val id: Long = 0L,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val address: String? = "",
    val userCategory: UserCategory = UserCategory.StartingStrong
)

fun UserDTO.toEntity() = UserEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    password = password,
    phoneNumber = phoneNumber,
    address = address,
    userCategory = userCategory.category
)

fun UserDTO.toLoggedInEntity() = LoggedInUserEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    password = password,
    phoneNumber = phoneNumber,
    address = address,
    userCategory = userCategory.category
)

fun UserInterface.toDTO() = UserDTO(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    password = password,
    phoneNumber = phoneNumber,
    address = address,
    userCategory = UserCategory.valueOf(userCategory)
)


