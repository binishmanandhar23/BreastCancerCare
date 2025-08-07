package com.breastcancer.breastcancercare.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.breastcancer.breastcancercare.database.local.AppDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("breast_cancer_care_database.db")

    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}