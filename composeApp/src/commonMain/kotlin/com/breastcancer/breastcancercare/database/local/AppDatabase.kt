package com.breastcancer.breastcancercare.database.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.breastcancer.breastcancercare.database.local.converters.BlogConverter
import com.breastcancer.breastcancercare.database.local.converters.FAQConverter
import com.breastcancer.breastcancercare.database.local.converters.ListConverter
import com.breastcancer.breastcancercare.database.local.dao.BlogDAO
import com.breastcancer.breastcancercare.database.local.dao.CalendarDAO
import com.breastcancer.breastcancercare.database.local.dao.FAQDAO
import com.breastcancer.breastcancercare.database.local.dao.UserDao
import com.breastcancer.breastcancercare.database.local.entity.BlogEntity
import com.breastcancer.breastcancercare.database.local.entity.CategoryEntity
import com.breastcancer.breastcancercare.database.local.entity.EventEntity
import com.breastcancer.breastcancercare.database.local.entity.FAQEntity
import com.breastcancer.breastcancercare.database.local.entity.LoggedInUserEntity
import com.breastcancer.breastcancercare.database.local.entity.ProgramEntity
import com.breastcancer.breastcancercare.database.local.entity.SuitabilityEntity
import com.breastcancer.breastcancercare.database.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [FAQEntity::class, ProgramEntity::class, EventEntity::class, UserEntity::class, LoggedInUserEntity::class, SuitabilityEntity::class, BlogEntity::class, CategoryEntity::class],
    version = 14
)
@TypeConverters(FAQConverter::class, BlogConverter::class, ListConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFAQDAO(): FAQDAO

    abstract fun getCalendarDAO(): CalendarDAO

    abstract fun getUserDAO(): UserDao

    abstract fun getBlogDAO(): BlogDAO
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getAppDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase =
    builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()

