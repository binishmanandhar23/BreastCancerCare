package com.breastcancer.breastcancercare.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breastcancer.breastcancercare.database.local.entity.FAQEntity
import com.breastcancer.breastcancercare.database.local.entity.SuitabilityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FAQDAO {
    @Query("SELECT * FROM faqentity")
    fun getAllFAQs(): Flow<List<FAQEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(faqs: List<FAQEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSuitabilities(faqs: List<SuitabilityEntity>)

    @Query("SELECT * FROM SuitabilityEntity ORDER BY name")
    fun getAllSuitabilities(): Flow<List<SuitabilityEntity>>
}