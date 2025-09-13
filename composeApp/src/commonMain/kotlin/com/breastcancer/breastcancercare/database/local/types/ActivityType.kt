package com.breastcancer.breastcancercare.database.local.types

import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType.Companion.LivingWellActivityTypeEnum
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType.DiscussionGroups
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType.MindfulRecoveryProgram
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType.Webinars
import com.breastcancer.breastcancercare.database.local.types.LivingWellActivityType.WellnessActivities
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType.Companion.StartingStrongActivityTypeEnum
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType.SupportGroups


interface ActivityType {
    val type: String
}
sealed class StartingStrongActivityType(override val type: String) : ActivityType {
    class SupportGroups(type: String = StartingStrongActivityTypeEnum.SupportGroups.type) :
        StartingStrongActivityType(type)

    class Workshops(type: String = StartingStrongActivityTypeEnum.Workshops.type) :
        StartingStrongActivityType(type)

    class Counselling(type: String = StartingStrongActivityTypeEnum.Counselling.type) :
        StartingStrongActivityType(type)

    class Nursing(type: String = StartingStrongActivityTypeEnum.Nursing.type) :
        StartingStrongActivityType(type)

    class FinancialAndPracticalHardshipSupport(type: String = StartingStrongActivityTypeEnum.FinancialAndPracticalHardshipSupport.type) :
        StartingStrongActivityType(type)

    companion object {
        val all: List<ActivityType> = listOf(
            SupportGroups(),
            Workshops(),
            Counselling(),
            Nursing(),
            FinancialAndPracticalHardshipSupport()
        )
        enum class StartingStrongActivityTypeEnum(val type: String) {
            SupportGroups("support_groups"),
            Workshops("starting_strong_workshops"),
            Counselling("counselling"),
            Nursing("nursing"),
            FinancialAndPracticalHardshipSupport("financial_and_practical_hardship_support")
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

    class Counselling(type: String = LivingWellActivityTypeEnum.Counselling.type) :
        LivingWellActivityType(type)

    class Nursing(type: String = LivingWellActivityTypeEnum.Nursing.type) :
        LivingWellActivityType(type)

    class FinancialAndPracticalHardshipSupport(type: String = LivingWellActivityTypeEnum.FinancialAndPracticalHardshipSupport.type) :
        LivingWellActivityType(type)

    companion object {
        val all: List<ActivityType> = listOf(
            DiscussionGroups(),
            Workshops(),
            Webinars(),
            WellnessActivities(),
            MindfulRecoveryProgram(),
            Counselling(),
            Nursing(),
            FinancialAndPracticalHardshipSupport()
        )
        enum class LivingWellActivityTypeEnum(val type: String) {
            DiscussionGroups("discussion_groups"),
            Workshops("living_well_workshops"),
            Webinars("webinars"),
            WellnessActivities("wellness_activities"),
            MindfulRecoveryProgram("mindful_recovery_program"),
            Counselling("counselling"),
            Nursing("nursing"),
            FinancialAndPracticalHardshipSupport("financial_and_practical_hardship_support")
        }
    }
}

object ActivityUtils {
    /** Normalize incoming keys like "Support-Groups" → "support_groups". */
    private fun norm(s: String) = s.trim().lowercase().replace('-', '_')

    /** Returns an *instance* of the matching subclass, or null if unknown. */
    fun fromType(category: UserCategory, type: String): ActivityType =
        if (category == UserCategory.StartingStrong) {
            when (norm(type)) {
                StartingStrongActivityTypeEnum.SupportGroups.type -> SupportGroups()
                StartingStrongActivityTypeEnum.Workshops.type -> StartingStrongActivityType.Workshops()
                StartingStrongActivityTypeEnum.Counselling.type -> StartingStrongActivityType.Counselling()
                StartingStrongActivityTypeEnum.Nursing.type -> StartingStrongActivityType.Nursing()
                StartingStrongActivityTypeEnum.FinancialAndPracticalHardshipSupport.type -> StartingStrongActivityType.FinancialAndPracticalHardshipSupport()
                else -> throw IllegalArgumentException("Unknown type: $type")
            }
        } else {
            when (norm(type)) {
                LivingWellActivityTypeEnum.DiscussionGroups.type -> DiscussionGroups()
                LivingWellActivityTypeEnum.Workshops.type -> LivingWellActivityType.Workshops()
                LivingWellActivityTypeEnum.Webinars.type -> Webinars()
                LivingWellActivityTypeEnum.WellnessActivities.type -> WellnessActivities()
                LivingWellActivityTypeEnum.MindfulRecoveryProgram.type -> MindfulRecoveryProgram()
                LivingWellActivityTypeEnum.Counselling.type -> LivingWellActivityType.Counselling()
                LivingWellActivityTypeEnum.Nursing.type -> LivingWellActivityType.Nursing()
                LivingWellActivityTypeEnum.FinancialAndPracticalHardshipSupport.type -> LivingWellActivityType.FinancialAndPracticalHardshipSupport()
                else -> throw IllegalArgumentException("Unknown type: $type")
            }
        }

    fun getActivityTypeLabel(type: ActivityType): String = when (type) {
        is SupportGroups -> "Support Groups"
        is StartingStrongActivityType.Workshops -> "Workshops"
        is StartingStrongActivityType.Counselling -> "Counselling"
        is StartingStrongActivityType.Nursing -> "Nursing"
        is StartingStrongActivityType.FinancialAndPracticalHardshipSupport -> "Financial and Practical Hardship Support"
        is DiscussionGroups -> "Discussion Groups"
        is LivingWellActivityType.Workshops -> "Workshops"
        is Webinars -> "Webinars"
        is WellnessActivities -> "Wellness Activities"
        is MindfulRecoveryProgram -> "Mindful Recovery Program"
        is LivingWellActivityType.Counselling -> "Counselling"
        is LivingWellActivityType.Nursing -> "Nursing"
        is LivingWellActivityType.FinancialAndPracticalHardshipSupport -> "Financial and Practical Hardship Support"
        else -> ""
    }
}

