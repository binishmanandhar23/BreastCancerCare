package com.breastcancer.breastcancercare.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breastcancer.breastcancercare.database.local.entity.ActivityEntity
import com.breastcancer.breastcancercare.database.local.entity.SuitabilityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDAO {
    @Query("SELECT * FROM activityentity WHERE category = :userCategory")
    fun getAllActivities(userCategory: String): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activityentity WHERE activityType = :activityType")
    fun getAllActivitiesByType(activityType: String): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activityentity WHERE startDate = :date")
    fun getEventsFromSelectedDate(date: String): Flow<List<ActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllActivities(events: List<ActivityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSuitabilities(suitabilities: List<SuitabilityEntity>)

    @Query("SELECT * FROM suitabilityentity")
    fun getAllSuitabilities(): Flow<List<SuitabilityEntity>>

    @Query("SELECT * FROM activityentity WHERE id = :id")
    suspend fun getActivityById(id: Long): ActivityEntity

}