package com.breastcancer.breastcancercare.database.local.expect

import com.breastcancer.breastcancercare.database.local.AppDatabase
import com.breastcancer.breastcancercare.database.local.getAppDatabase
import com.breastcancer.breastcancercare.database.getDatabaseBuilder
import com.breastcancer.breastcancercare.database.local.dao.UserDao
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getAppDatabase(builder)
    }
    single<UserDao> { get<AppDatabase>().getUserDAO() }
}