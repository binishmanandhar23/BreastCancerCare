package com.breastcancer.breastcancercare.di

import com.breastcancer.breastcancercare.database.local.common.commonModule
import com.breastcancer.breastcancercare.database.local.expect.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            commonModule() + platformModule()
        )
    }
}