package com.breastcancer.breastcancercare.database.local.types

enum class UserCategory(val category: String) {
    StartingStrong("starting_strong"),
    LivingWell("living_well");

    companion object{
        fun fromCategory(category: String) = entries.find { it.category == category } ?: StartingStrong
    }
}

