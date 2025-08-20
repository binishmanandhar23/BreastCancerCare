package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val address: String?
)
