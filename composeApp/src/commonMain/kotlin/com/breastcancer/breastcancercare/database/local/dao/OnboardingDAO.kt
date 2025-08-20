package com.breastcancer.breastcancercare.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breastcancer.breastcancercare.database.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OnboardingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM userentity WHERE email = :email")
    fun getUser(email: String): Flow<UserEntity?>
}