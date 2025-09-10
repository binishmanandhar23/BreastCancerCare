package com.breastcancer.breastcancercare.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.breastcancer.breastcancercare.models.interfaces.UserInterface

@Entity(indices = [Index(value = ["email"], unique = true)])
data class UserEntity(
    @PrimaryKey override val id: Long,
    override val firstName: String,
    override val lastName: String,
    override val password: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) override  val email: String,
    override val phoneNumber: String,
    override val address: String?,
    override val userCategory: String,
): UserInterface
