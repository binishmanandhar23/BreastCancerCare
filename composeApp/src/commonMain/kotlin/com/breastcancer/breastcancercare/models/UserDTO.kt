package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.UserEntity

data class UserDTO(
    val id: Long,
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val address: String?
)

fun UserDTO.toEntity() = UserEntity(
    id = id,
    name = name,
    email = email,
    password = password,
    phoneNumber = phoneNumber,
    address = address
)

fun UserEntity.toDTO() = UserDTO(
    id = id,
    name = name,
    email = email,
    password = password,
    phoneNumber = phoneNumber,
    address = address
)
