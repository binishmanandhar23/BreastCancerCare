package com.breastcancer.breastcancercare.database.local.types

enum class UserCategory(val category: String) {
    Undefined("undefined"),
    StartingStrong("starting_strong"),
    LivingWell("living_well");

    companion object{
        fun fromCategory(category: String?) = entries.find { it.category == category }?: Undefined

        fun getLabel(category: UserCategory) = when(category){
            Undefined -> "Undefined"
            StartingStrong -> "Starting Strong"
            LivingWell -> "Living Well"
        }
    }
}

