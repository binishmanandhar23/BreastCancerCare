package com.breastcancer.breastcancercare.database.local.common

import com.breastcancer.breastcancercare.database.local.AppDatabase
import com.breastcancer.breastcancercare.repo.FAQRepository
import com.breastcancer.breastcancercare.viewmodel.FAQViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun commonModule(): Module = module {
    single<FAQRepository> { FAQRepository(get<AppDatabase>().getFAQDAO()) }
    singleOf(::FAQViewModel)
}