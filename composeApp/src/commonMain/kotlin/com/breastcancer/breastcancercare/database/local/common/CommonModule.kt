package com.breastcancer.breastcancercare.database.local.common

import com.breastcancer.breastcancercare.database.local.AppDatabase
import com.breastcancer.breastcancercare.database.local.dao.FAQDAO
import org.koin.core.module.Module
import org.koin.dsl.module

fun commonModule(): Module = module { single<FAQDAO> {get<AppDatabase>().getFAQDAO()} }