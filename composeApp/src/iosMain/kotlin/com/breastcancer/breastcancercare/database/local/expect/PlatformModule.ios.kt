package com.breastcancer.breastcancercare.database.local.expect

import com.breastcancer.breastcancercare.database.local.AppDatabase
import com.breastcancer.breastcancercare.database.local.getAppDatabase
import com.breastcancer.breastcancercare.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder()
        getAppDatabase(builder)
    }

}