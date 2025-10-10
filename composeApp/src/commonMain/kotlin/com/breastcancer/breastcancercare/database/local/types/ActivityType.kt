package com.breastcancer.breastcancercare.database.local.types

import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType.Companion.LivingWellActivityTypeEnum
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType.DiscussionGroups
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType.MindfulRecoveryProgram
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType.Webinars
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType.WellnessActivities
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType.Companion.StartingStrongActivityTypeEnum
import com.breastcancer.breastcancercare.database.local.types.GeneralActivityType.Companion.GeneralActivityTypeEnum
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType.SupportGroups
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType.Workshops


interface ActivityType {
    val type: String
}

sealed class StartingStrongActivityType(override val type: String) : ActivityType {
    class SupportGroups(type: String = StartingStrongActivityTypeEnum.SupportGroups.type) :
        StartingStrongActivityType(type)

    class Workshops(type: String = StartingStrongActivityTypeEnum.Workshops.type) :
        StartingStrongActivityType(type)


    companion object {
        val all: List<ActivityType> = listOf(
            SupportGroups(),
            Workshops()
        )

        enum class StartingStrongActivityTypeEnum(val type: String) {
            SupportGroups("support_groups"),
            Workshops("starting_strong_workshops")
        }
    }
}

sealed class LivingWellActivityType(override val type: String) : ActivityType {
    class DiscussionGroups(type: String = LivingWellActivityTypeEnum.DiscussionGroups.type) :
        LivingWellActivityType(type)

    class Workshops(type: String = LivingWellActivityTypeEnum.Workshops.type) :
        LivingWellActivityType(type)

    class Webinars(type: String = LivingWellActivityTypeEnum.Webinars.type) :
        LivingWellActivityType(type)

    class WellnessActivities(type: String = LivingWellActivityTypeEnum.WellnessActivities.type) :
        LivingWellActivityType(type)

    class MindfulRecoveryProgram(type: String = LivingWellActivityTypeEnum.MindfulRecoveryProgram.type) :
        LivingWellActivityType(type)

    companion object {
        val all: List<ActivityType> = listOf(
            DiscussionGroups(),
            Workshops(),
            Webinars(),
            WellnessActivities(),
            MindfulRecoveryProgram(),
        )

        enum class LivingWellActivityTypeEnum(val type: String) {
            DiscussionGroups("discussion_groups"),
            Workshops("living_well_workshops"),
            Webinars("webinars"),
            WellnessActivities("wellness_activities"),
            MindfulRecoveryProgram("mindful_recovery_program"),
        }
    }
}

sealed class GeneralActivityType(override val type: String): ActivityType{
    class Counselling(type: String = GeneralActivityTypeEnum.Counselling.type) :
        GeneralActivityType(type)

    class Nursing(type: String = GeneralActivityTypeEnum.Nursing.type) :
        GeneralActivityType(type)

    class FinancialAndPracticalHardshipSupport(type: String = GeneralActivityTypeEnum.FinancialAndPracticalHardshipSupport.type) :
        GeneralActivityType(type)
    companion object {
        val all: List<ActivityType> = listOf(
            Nursing(),
            Counselling(),
            FinancialAndPracticalHardshipSupport()
        )

        enum class GeneralActivityTypeEnum(val type: String) {
            Counselling("counselling"), //Same for both Starting Strong and Living Well
            Nursing("nursing"), //Same for both Starting Strong and Living Well
            FinancialAndPracticalHardshipSupport("financial_and_practical_hardship_support") //Same for both Starting Strong and Living Well
        }
    }
}

object ActivityUtils {
    /** Normalize incoming keys like "Support-Groups" → "support_groups". */
    private fun norm(s: String) = s.trim().lowercase().replace('-', '_')

    /** Returns an *instance* of the matching subclass, or null if unknown. */
    fun fromType(category: UserCategory? = null, type: String): ActivityType =
        when (category) {
            null, UserCategory.Undefined -> when(norm(type)){
                GeneralActivityTypeEnum.Counselling.type -> GeneralActivityType.Counselling()
                GeneralActivityTypeEnum.Nursing.type -> GeneralActivityType.Nursing()
                GeneralActivityTypeEnum.FinancialAndPracticalHardshipSupport.type -> GeneralActivityType.FinancialAndPracticalHardshipSupport()
                else -> throw IllegalArgumentException("Unknown type: $type")
            }

            UserCategory.StartingStrong -> {
                when (norm(type)) {
                    StartingStrongActivityTypeEnum.SupportGroups.type -> SupportGroups()
                    StartingStrongActivityTypeEnum.Workshops.type -> Workshops()
                    GeneralActivityTypeEnum.Counselling.type -> GeneralActivityType.Counselling()
                    GeneralActivityTypeEnum.Nursing.type -> GeneralActivityType.Nursing()
                    GeneralActivityTypeEnum.FinancialAndPracticalHardshipSupport.type -> GeneralActivityType.FinancialAndPracticalHardshipSupport()
                    else -> throw IllegalArgumentException("Unknown type: $type")
                }
            }

            UserCategory.LivingWell -> {
                when (norm(type)) {
                    LivingWellActivityTypeEnum.DiscussionGroups.type -> DiscussionGroups()
                    LivingWellActivityTypeEnum.Workshops.type -> LivingWellActivityType.Workshops()
                    LivingWellActivityTypeEnum.Webinars.type -> Webinars()
                    LivingWellActivityTypeEnum.WellnessActivities.type -> WellnessActivities()
                    LivingWellActivityTypeEnum.MindfulRecoveryProgram.type -> MindfulRecoveryProgram()
                    GeneralActivityTypeEnum.Counselling.type -> GeneralActivityType.Counselling()
                    GeneralActivityTypeEnum.Nursing.type -> GeneralActivityType.Nursing()
                    GeneralActivityTypeEnum.FinancialAndPracticalHardshipSupport.type -> GeneralActivityType.FinancialAndPracticalHardshipSupport()
                    else -> throw IllegalArgumentException("Unknown type: $type")
                }
            }
        }

    fun getActivityTypeLabel(type: ActivityType): String = when (type) {
        is SupportGroups -> "Support Groups"
        is Workshops -> "Workshops"
        is GeneralActivityType.Counselling -> "Counselling"
        is GeneralActivityType.Nursing -> "Nursing"
        is GeneralActivityType.FinancialAndPracticalHardshipSupport -> "Financial and Practical Hardship Support"
        is DiscussionGroups -> "Discussion Groups"
        is LivingWellActivityType.Workshops -> "Workshops"
        is Webinars -> "Webinars"
        is WellnessActivities -> "Wellness Activities"
        is MindfulRecoveryProgram -> "Mindful Recovery Program"
        else -> ""
    }
}

