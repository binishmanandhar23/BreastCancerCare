package com.breastcancer.breastcancercare.utils

import com.breastcancer.breastcancercare.screens.Screens
import com.breastcancer.breastcancercare.screens.SubScreens

fun getNavigationRoute(mainScreen: Screens, subScreen: SubScreens? = null): String =
    "${mainScreen.screen}${if (subScreen != null) "/" + subScreen.screen else ""}"