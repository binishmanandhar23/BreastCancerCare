package com.breastcancer.breastcancercare.screens

import com.breastcancer.breastcancercare.database.local.types.UserCategory
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object BaseGraph : Route

    @Serializable
    data object Splash : Route

    @Serializable
    data object Onboarding : Route {
        @Serializable
        data object Register : Route
    }

    @Serializable
    data object Main : Route {
        @Serializable
        data object Profile : Route

        @Serializable
        data object About : Route

        @Serializable
        data object Contact : Route

        @Serializable
        data object EditProfile : Route

        @Serializable
        data class BlogDetail(val slug: String) : Route

        @Serializable
        data object AllBlogs : Route

        @Serializable
        data object AllActivities : Route

        @Serializable
        data class ActivityDetail(val id: Long) : Route
        @Serializable
        data class GeneralActivityDetail(val type: String) : Route

        @Serializable
        data object AllActivityHistory : Route

        @Serializable
        data class SurveyRoute(val id: Long) : Route

        @Serializable
        data class SurveyMandatoryDialog(val id: Long) : Route
    }

    @Serializable
    data class Journey(
        val userId: Long,
        val userCategory: String = UserCategory.Undefined.category,
        val hideBackButton: Boolean = true
    ) : Route {
        @Serializable
        data class JourneyDetail(val userId: Long, val userCategory: String) : Route
    }

    @Serializable
    data class TutorialScreen(val userId: Long, val hideSkipButton: Boolean = false): Route
}