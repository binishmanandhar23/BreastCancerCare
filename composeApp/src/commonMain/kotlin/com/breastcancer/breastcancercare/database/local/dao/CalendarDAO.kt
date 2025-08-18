package com.breastcancer.breastcancercare.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breastcancer.breastcancercare.database.local.entity.EventEntity
import com.breastcancer.breastcancercare.database.local.entity.ProgramEntity
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

    @Query("SELECT * FROM programentity WHERE date = :date")
    fun getProgramsFromSelectedDate(date: String): Flow<List<ProgramEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPrograms(events: List<ProgramEntity>)
}