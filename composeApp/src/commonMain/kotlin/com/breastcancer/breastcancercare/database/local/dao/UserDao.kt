package com.breastcancer.breastcancercare.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breastcancer.breastcancercare.database.local.entity.LoggedInUserEntity
import com.breastcancer.breastcancercare.database.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM userentity WHERE email = :email")
    suspend fun getUser(email: String): UserEntity?

    @Query("SELECT COALESCE(MAX(id), 0) FROM userentity")
    suspend fun getMaxId(): Long

    @Query("""
    SELECT EXISTS(
        SELECT 1 FROM userentity 
        WHERE email = :email COLLATE NOCASE 
        LIMIT 1
    )""")
    suspend fun emailExistsIgnoreCase(email: String): Boolean

    @Query("""
SELECT EXISTS(
    SELECT 1 FROM userentity
    WHERE email = :email COLLATE NOCASE
      AND id != :excludeId
    LIMIT 1
)
""")
    suspend fun emailExistsForOtherUser(email: String, excludeId: Long): Boolean


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setLoggedInUser(userEntity: LoggedInUserEntity)

    @Query("SELECT * FROM loggedinuserentity LIMIT 1")
    fun getLoggedInUser(): Flow<LoggedInUserEntity?>

    @Query("DELETE FROM loggedinuserentity")
    suspend fun deleteLoggedInUser()

    @Query("SELECT COUNT(1) > 0 FROM loggedinuserentity")
    suspend fun isLoggedIn(): Boolean
}