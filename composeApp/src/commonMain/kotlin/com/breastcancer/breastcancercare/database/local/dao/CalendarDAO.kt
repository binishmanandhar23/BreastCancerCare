package com.breastcancer.breastcancercare.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breastcancer.breastcancercare.database.local.entity.EventEntity
import com.breastcancer.breastcancercare.database.local.entity.ProgramEntity
import com.breastcancer.breastcancercare.database.local.entity.SuitabilityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDAO {
    @Query("SELECT * FROM evententity")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM evententity WHERE date = :date")
    fun getEventsFromSelectedDate(date: String): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEvents(events: List<EventEntity>)

    @Query("SELECT * FROM programentity")
    fun getAllPrograms(): Flow<List<ProgramEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPrograms(events: List<ProgramEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSuitabilities(suitabilities: List<SuitabilityEntity>)

    @Query("SELECT * FROM suitabilityentity")
    fun getAllSuitabilities(): Flow<List<SuitabilityEntity>>

}