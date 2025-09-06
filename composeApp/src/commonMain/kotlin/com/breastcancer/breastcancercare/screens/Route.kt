package com.breastcancer.breastcancercare.screens

import com.breastcancer.breastcancercare.models.BlogDTO
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Splash: Route
    @Serializable
    data object Onboarding: Route {
        @Serializable
        data object Register: Route
    }
    @Serializable
    data object Main: Route {
        @Serializable
        data object Profile: Route
        @Serializable
        data object About: Route
        @Serializable
        data object Contact: Route
        @Serializable
        data object EditProfile: Route
        @Serializable
        data class BlogDetail(val slug: String): Route
    }
}