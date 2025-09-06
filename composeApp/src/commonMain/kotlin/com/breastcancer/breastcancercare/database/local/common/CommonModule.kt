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
import com.breastcancer.breastcancercare.viewmodel.SplashViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.breastcancer.breastcancercare.database.local.dao.UserDao
import com.breastcancer.breastcancercare.repo.BlogRepository
import com.breastcancer.breastcancercare.viewmodel.BlogViewModel
import com.breastcancer.breastcancercare.viewmodel.EditProfileViewModel
import com.breastcancer.breastcancercare.viewmodel.ProfileViewModel

fun commonModule(): Module = module {
    single<FAQRepository> { FAQRepository(get<AppDatabase>().getFAQDAO()) }
    single<CalendarRepository> { CalendarRepository(get<AppDatabase>().getCalendarDAO()) }
    single<OnboardingRepository> { OnboardingRepository(get<AppDatabase>().getUserDAO()) }
    single<HomeRepository> { HomeRepository(userDao = get<AppDatabase>().getUserDAO(), blogDAO = get<AppDatabase>().getBlogDAO(), calendarDAO = get<AppDatabase>().getCalendarDAO()) }
    single<BlogRepository> { BlogRepository(blogDAO = get<AppDatabase>().getBlogDAO()) }
    single<UserDao> { get<AppDatabase>().getUserDAO() }


    singleOf(::FAQViewModel)
    singleOf(::CalendarViewModel)
    singleOf(::OnboardingViewModel)
    singleOf(::PermissionViewModel)
    singleOf(::HomeViewModel)
    singleOf(::SplashViewModel)
    singleOf(::EditProfileViewModel)
    singleOf(::ProfileViewModel)
    singleOf(::BlogViewModel)
}