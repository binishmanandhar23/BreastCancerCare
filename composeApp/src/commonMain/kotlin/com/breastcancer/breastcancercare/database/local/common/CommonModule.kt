package com.breastcancer.breastcancercare.database.local.common

import com.breastcancer.breastcancercare.database.local.AppDatabase
import com.breastcancer.breastcancercare.repo.CalendarRepository
import com.breastcancer.breastcancercare.repo.FAQRepository
import com.breastcancer.breastcancercare.repo.HomeRepository
import com.breastcancer.breastcancercare.repo.OnboardingRepository
import com.breastcancer.breastcancercare.viewmodel.CalendarViewModel
import com.breastcancer.breastcancercare.viewmodel.FAQViewModel
import com.breastcancer.breastcancercare.viewmodel.HomeViewModel
import com.breastcancer.breastcancercare.viewmodel.OnboardingViewModel
import com.breastcancer.breastcancercare.viewmodel.PermissionViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun commonModule(): Module = module {
    single<FAQRepository> { FAQRepository(get<AppDatabase>().getFAQDAO()) }
    single<CalendarRepository> { CalendarRepository(get<AppDatabase>().getCalendarDAO()) }
    single<OnboardingRepository> { OnboardingRepository(get<AppDatabase>().getUserDAO()) }
    single<HomeRepository> { HomeRepository(get<AppDatabase>().getUserDAO()) }


    singleOf(::FAQViewModel)
    singleOf(::CalendarViewModel)
    singleOf(::OnboardingViewModel)
    singleOf(::PermissionViewModel)
    singleOf(::HomeViewModel)
}