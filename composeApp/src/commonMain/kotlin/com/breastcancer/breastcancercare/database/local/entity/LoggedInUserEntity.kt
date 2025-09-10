package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breastcancer.breastcancercare.models.interfaces.UserInterface

@Entity
data class LoggedInUserEntity(
    @PrimaryKey override val id: Long,
    override val firstName: String,
    override val lastName: String,
    override val password: String,
    override val email: String,
    override val phoneNumber: String,
    override val address: String?,
    override val userCategory: String
): UserInterface
