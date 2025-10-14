package com.breastcancer.breastcancercare.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breastcancer.breastcancercare.database.local.entity.ActivityEntity
import com.breastcancer.breastcancercare.database.local.entity.ActivityScheduleEntity
import com.breastcancer.breastcancercare.database.local.entity.ActivityScheduleWithActivityEntity
import com.breastcancer.breastcancercare.database.local.entity.SuitabilityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDAO {
    @Query("SELECT * FROM activityentity WHERE category = :userCategory")
    fun getAllActivities(userCategory: String): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activityentity WHERE category = :userCategory OR category IS null") //null = General Activities included
    fun getAllActivitiesAndGeneralActivities(userCategory: String?): Flow<List<ActivityEntity>>

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityHistoryEntity(activityScheduleEntity: ActivityScheduleEntity)

    @Query("SELECT * FROM activityscheduleentity WHERE userId = :userId")
    fun getAllActivityHistoryWithActivity(userId: Long?): Flow<List<ActivityScheduleWithActivityEntity>>

    @Query("SELECT * FROM activityscheduleentity WHERE activityId = :activityId AND userId = :userId")
    fun getActivityScheduleByActivityId(activityId: Long?, userId: Long?): Flow<List<ActivityScheduleWithActivityEntity>?>
    @Query("SELECT * FROM activityscheduleentity WHERE activityId = :activityId AND userId = :userId AND registeredForDate = :registeredDate")
    fun getActivityScheduleByActivityIdAndRegisteredDate(activityId: Long, userId: Long?, registeredDate: String): Flow<ActivityScheduleEntity?>

    @Query("SELECT * FROM activityscheduleentity WHERE userId = :userId AND registeredForDate = :registeredDate")
    fun getActivityScheduleByRegisteredDate(
        userId: Long?,
        registeredDate: String?
    ): Flow<List<ActivityScheduleEntity>>
}