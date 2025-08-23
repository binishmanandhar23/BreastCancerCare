package com.breastcancer.breastcancercare.permission

import dev.icerock.moko.permissions.PermissionsController
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun permissionPlatformModule(): Module = module {
    single<PermissionsController> {
        PermissionsController(androidContext())
    }
}