package com.breastcancer.breastcancercare

import android.app.Application
import com.breastcancer.breastcancercare.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            appDeclaration = { androidContext(this@MainApplication) },
        )
    }
}